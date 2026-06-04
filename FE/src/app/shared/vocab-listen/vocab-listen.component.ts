import { Component, Input, computed, inject, signal } from '@angular/core';
import {
  SequenceController,
  SequenceItem,
  TtsService,
} from '../../services/tts.service';

export interface ListenItem {
  word: string;
  vi: string;
}

interface SeqMeta {
  vocabIndex: number;
  part: 'en' | 'vi';
}

@Component({
  selector: 'app-vocab-listen',
  standalone: true,
  templateUrl: './vocab-listen.component.html',
  styleUrl: './vocab-listen.component.scss',
})
export class VocabListenComponent {
  readonly tts = inject(TtsService);

  @Input({ required: true }) items: ListenItem[] = [];

  readonly playingIndex = signal<number | null>(null);
  readonly playingAll = signal(false);
  readonly loop = signal(false);
  readonly settingsOpen = signal(false);

  readonly enVoices = computed(() => this.tts.voicesByLang()['en-US'] || []);
  readonly viVoices = computed(() => this.tts.voicesByLang()['vi-VN'] || []);

  readonly enSelected = computed(
    () => this.tts.selectedVoiceURI()['en-US'] ?? ''
  );
  readonly viSelected = computed(
    () => this.tts.selectedVoiceURI()['vi-VN'] ?? ''
  );

  readonly enActiveName = computed(
    () => this.tts.getActiveVoiceFor('en-US')?.name ?? '(không có)'
  );
  readonly viActiveName = computed(
    () => this.tts.getActiveVoiceFor('vi-VN')?.name ?? '(không có)'
  );

  readonly enStaticCount = computed(() => this.tts.staticCount()['en-US']);
  readonly viStaticCount = computed(() => this.tts.staticCount()['vi-VN']);

  private controller: SequenceController | null = null;
  private stopRequested = false;

  togglePlayAll(): void {
    if (this.playingAll()) this.stop();
    else this.playAll();
  }

  toggleLoop(): void {
    this.loop.update((v) => !v);
  }

  toggleSettings(): void {
    this.settingsOpen.update((v) => !v);
  }

  onVoiceChange(lang: string, event: Event): void {
    const select = event.target as HTMLSelectElement;
    this.tts.setVoice(lang, select.value || null);
  }

  playOne(idx: number): void {
    this.stop();
    const item = this.items[idx];
    if (!item) return;
    const seq = this.buildSequence([item], idx);
    this.controller = this.tts.speakSequence<SeqMeta>(seq, (it) => {
      if (it.meta) this.playingIndex.set(it.meta.vocabIndex);
    });
    this.controller.done.then(() => {
      if (!this.stopRequested) this.playingIndex.set(null);
    });
  }

  private playAll(): void {
    this.stopRequested = false;
    this.playingAll.set(true);
    this.runOnce();
  }

  private runOnce(): void {
    if (this.stopRequested) return;
    const seq = this.buildSequence(this.items, 0);
    this.controller = this.tts.speakSequence<SeqMeta>(seq, (it) => {
      if (it.meta) this.playingIndex.set(it.meta.vocabIndex);
    });
    this.controller.done.then(() => {
      if (this.stopRequested) return;
      if (this.loop()) {
        this.runOnce();
      } else {
        this.playingAll.set(false);
        this.playingIndex.set(null);
      }
    });
  }

  stop(): void {
    this.stopRequested = true;
    this.controller?.cancel();
    this.controller = null;
    this.playingAll.set(false);
    this.playingIndex.set(null);
  }

  private buildSequence(
    items: ListenItem[],
    startIndex: number
  ): SequenceItem<SeqMeta>[] {
    const seq: SequenceItem<SeqMeta>[] = [];
    items.forEach((it, k) => {
      const vocabIndex = startIndex + k;
      seq.push({
        text: it.word,
        lang: 'en-US',
        pauseAfter: 250,
        meta: { vocabIndex, part: 'en' },
      });
      seq.push({
        text: it.vi,
        lang: 'vi-VN',
        pauseAfter: 600,
        meta: { vocabIndex, part: 'vi' },
      });
    });
    return seq;
  }
}
