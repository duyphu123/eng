import { Injectable, NgZone, computed, inject, signal } from '@angular/core';

export interface SequenceItem<T = unknown> {
  text: string;
  lang?: string;
  pauseAfter?: number;
  meta?: T;
}

export interface SequenceController {
  cancel: () => void;
  done: Promise<void>;
}

interface PlaybackHandle {
  promise: Promise<void>;
  cancel: () => void;
}

const STORAGE_KEY = 'eng-toeic.tts-voices';
const MANIFEST_URL = '/assets/audio/manifest.json';

type ManifestShape = Record<string, Record<string, string>>; // { lang: { text: filename } }

@Injectable({ providedIn: 'root' })
export class TtsService {
  readonly defaultLang = 'en-US';

  // Playback rate applied to `<audio>` element (pre-recorded MP3) AND
  // `SpeechSynthesisUtterance.rate` (Web Speech fallback). 1.0 = HoaiMy/Aria's
  // natural pace; bump VI above 1.0 because the recorded pace is calm.
  private readonly rateByLang: Record<string, number> = {
    'en-US': 0.95,
    'vi-VN': 1.15,
  };

  private static readonly CACHED_LANGS = ['en-US', 'vi-VN'];

  private readonly zone = inject(NgZone);
  private readonly currentText = signal<string | null>(null);
  private generation = 0;
  private warmedUp = false;
  private currentPlayback: PlaybackHandle | null = null;
  private audioEl: HTMLAudioElement | null = null;

  /** Pre-recorded audio manifest (text → filename per lang). Loaded once. */
  readonly manifest = signal<ManifestShape>({});

  readonly voicesByLang = signal<Record<string, SpeechSynthesisVoice[]>>({});
  readonly selectedVoiceURI = signal<Record<string, string | null>>({});

  /** Lookup of how many texts per lang have static pre-recorded audio. */
  readonly staticCount = computed(() => ({
    'en-US': Object.keys(this.manifest()['en-US'] || {}).length,
    'vi-VN': Object.keys(this.manifest()['vi-VN'] || {}).length,
  }));

  constructor() {
    if (typeof window === 'undefined' || !('speechSynthesis' in window)) return;

    try {
      const raw = localStorage.getItem(STORAGE_KEY);
      if (raw) this.selectedVoiceURI.set(JSON.parse(raw));
    } catch {
      /* ignore */
    }

    this.refreshVoice();
    window.speechSynthesis.addEventListener('voiceschanged', () =>
      this.refreshVoice()
    );
    this.loadManifest();

    const prewarm = () => {
      if (this.warmedUp) return;
      this.warmedUp = true;
      const u = new SpeechSynthesisUtterance('a');
      u.volume = 0;
      u.lang = this.defaultLang;
      const v = this.getActiveVoice(this.defaultLang);
      if (v) u.voice = v;
      window.speechSynthesis.speak(u);
    };
    document.addEventListener('pointerdown', prewarm, {
      once: true,
      capture: true,
    });
    document.addEventListener('keydown', prewarm, {
      once: true,
      capture: true,
    });
  }

  // ─── Manifest ────────────────────────────────────────────

  private async loadManifest(): Promise<void> {
    try {
      const res = await fetch(MANIFEST_URL);
      if (!res.ok) return;
      const data = (await res.json()) as ManifestShape;
      this.zone.run(() => this.manifest.set(data));
    } catch {
      /* manifest is optional — silent if missing */
    }
  }

  private getStaticUrl(text: string, lang: string): string | null {
    const filename = this.manifest()[lang]?.[text];
    return filename ? `/assets/audio/${lang}/${filename}` : null;
  }

  // ─── Voice management ────────────────────────────────────

  private refreshVoice(): void {
    const allVoices = window.speechSynthesis.getVoices();
    const grouped: Record<string, SpeechSynthesisVoice[]> = {};
    for (const lang of TtsService.CACHED_LANGS) {
      const prefix = lang.split('-')[0];
      const matching = allVoices.filter(
        (v) => v.lang === lang || v.lang.startsWith(prefix)
      );
      grouped[lang] = this.rankVoices(matching, lang);
    }
    this.zone.run(() => this.voicesByLang.set(grouped));
  }

  private rankVoices(
    voices: SpeechSynthesisVoice[],
    lang: string
  ): SpeechSynthesisVoice[] {
    const score = (v: SpeechSynthesisVoice): number => {
      let s = 0;
      if (v.lang === lang) s += 100;
      if (/Natural|Neural/i.test(v.name)) s += 60;
      if (/Online/i.test(v.name)) s += 40;
      if (/Enhanced|Premium/i.test(v.name)) s += 50;
      if (/Google/i.test(v.name)) s += 30;
      if (v.default) s += 10;
      if (v.localService) s += 5;
      return s;
    };
    return [...voices].sort((a, b) => score(b) - score(a));
  }

  private getActiveVoice(lang: string): SpeechSynthesisVoice | null {
    const userURI = this.selectedVoiceURI()[lang];
    const list = this.voicesByLang()[lang] || [];
    if (userURI) {
      const match = list.find((v) => v.voiceURI === userURI);
      if (match) return match;
    }
    return list[0] ?? null;
  }

  getActiveVoiceFor(lang: string): SpeechSynthesisVoice | null {
    return this.getActiveVoice(lang);
  }

  setVoice(lang: string, voiceURI: string | null): void {
    const next = { ...this.selectedVoiceURI(), [lang]: voiceURI };
    this.selectedVoiceURI.set(next);
    try {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(next));
    } catch {
      /* ignore */
    }
  }

  // ─── Public speak API ───────────────────────────────────

  speak(text: string, lang: string = this.defaultLang): void {
    if (typeof window === 'undefined') return;
    this.generation++;
    const myGen = this.generation;
    this.cancelCurrent();
    this.zone.run(() => this.currentText.set(text));

    const playback = this.playOne(text, lang);
    this.currentPlayback = playback;
    playback.promise.then(() => {
      if (myGen !== this.generation) return;
      this.zone.run(() => this.currentText.set(null));
    });
  }

  speakSequence<T>(
    items: SequenceItem<T>[],
    onStart?: (item: SequenceItem<T>, index: number) => void
  ): SequenceController {
    const noop: SequenceController = {
      cancel: () => {},
      done: Promise.resolve(),
    };
    if (typeof window === 'undefined') return noop;
    if (!items.length) return noop;

    this.cancelCurrent();
    this.generation++;
    const seqGen = this.generation;

    let cancelled = false;
    let i = 0;
    let resolveDone!: () => void;
    const done = new Promise<void>((r) => {
      resolveDone = r;
    });

    const finish = () => {
      if (seqGen === this.generation) {
        this.zone.run(() => this.currentText.set(null));
      }
      resolveDone();
    };

    const next = () => {
      if (cancelled || seqGen !== this.generation) {
        finish();
        return;
      }
      if (i >= items.length) {
        finish();
        return;
      }
      const item = items[i];
      const lang = item.lang ?? this.defaultLang;
      this.zone.run(() => {
        this.currentText.set(item.text);
        onStart?.(item, i);
      });

      const playback = this.playOne(item.text, lang);
      this.currentPlayback = playback;
      playback.promise.then(() => {
        if (cancelled || seqGen !== this.generation) {
          finish();
          return;
        }
        i++;
        if (item.pauseAfter && item.pauseAfter > 0) {
          setTimeout(next, item.pauseAfter);
        } else {
          next();
        }
      });
    };

    next();

    return {
      cancel: () => {
        if (cancelled) return;
        cancelled = true;
        this.cancelCurrent();
        finish();
      },
      done,
    };
  }

  stop(): void {
    this.generation++;
    this.cancelCurrent();
    this.zone.run(() => this.currentText.set(null));
  }

  isSpeaking(text: string): boolean {
    return this.currentText() === text;
  }

  // ─── Internal: pick the right playback path ─────────────

  private cancelCurrent(): void {
    if (typeof window !== 'undefined' && 'speechSynthesis' in window) {
      const ss = window.speechSynthesis;
      if (ss.speaking || ss.pending) ss.cancel();
    }
    this.currentPlayback?.cancel();
    this.currentPlayback = null;
  }

  /**
   * Priority:
   *   1. Pre-recorded MP3 via manifest (best quality, offline, no CORS)
   *   2. Web Speech API with native voice
   *   3. Silent no-op (logged warning)
   */
  private playOne(text: string, lang: string): PlaybackHandle {
    const staticUrl = this.getStaticUrl(text, lang);
    if (staticUrl) return this.playViaAudio(staticUrl, lang);

    const voice = this.getActiveVoice(lang);
    if (voice) return this.playViaWebSpeech(text, lang, voice);

    // eslint-disable-next-line no-console
    console.warn(`[TTS] No audio source for "${text}" (${lang})`);
    return { promise: Promise.resolve(), cancel: () => {} };
  }

  private playViaWebSpeech(
    text: string,
    lang: string,
    voice: SpeechSynthesisVoice
  ): PlaybackHandle {
    let resolve!: () => void;
    let settled = false;
    const promise = new Promise<void>((r) => {
      resolve = r;
    });

    const u = new SpeechSynthesisUtterance(text);
    u.lang = lang;
    u.rate = this.rateByLang[lang] ?? 0.9;
    u.pitch = 1;
    u.voice = voice;

    const finish = () => {
      if (settled) return;
      settled = true;
      resolve();
    };
    u.onend = finish;
    u.onerror = finish;

    window.speechSynthesis.speak(u);
    window.speechSynthesis.resume();

    return {
      promise,
      cancel: () => {
        finish();
        try {
          window.speechSynthesis.cancel();
        } catch {
          /* ignore */
        }
      },
    };
  }

  private playViaAudio(url: string, lang: string): PlaybackHandle {
    if (!this.audioEl) {
      this.audioEl = new Audio();
      this.audioEl.preload = 'auto';
    }
    const audio = this.audioEl;

    let resolve!: () => void;
    let settled = false;
    const promise = new Promise<void>((r) => {
      resolve = r;
    });

    audio.src = url;
    audio.playbackRate = this.rateByLang[lang] ?? 0.9;
    audio.currentTime = 0;

    const finish = () => {
      if (settled) return;
      settled = true;
      audio.onended = null;
      audio.onerror = null;
      resolve();
    };
    audio.onended = finish;
    audio.onerror = (e) => {
      // eslint-disable-next-line no-console
      console.warn('[TTS] Static audio failed:', url, e);
      finish();
    };

    audio.play().catch((err: unknown) => {
      // eslint-disable-next-line no-console
      console.warn('[TTS] Static audio play() rejected:', url, err);
      finish();
    });

    return {
      promise,
      cancel: () => {
        try {
          audio.pause();
        } catch {
          /* ignore */
        }
        finish();
      },
    };
  }
}
