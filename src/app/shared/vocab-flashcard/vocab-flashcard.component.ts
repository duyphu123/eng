import {
  Component,
  HostListener,
  Input,
  OnChanges,
  SimpleChanges,
  computed,
  inject,
  signal,
} from '@angular/core';
import { TtsService } from '../../services/tts.service';
import { TooltipTextComponent } from '../tooltip/tooltip-text.component';

export interface FlashcardItem {
  word: string;
  vi: string;
  ipa?: string;
  example?: string;
  exampleVi?: string;
}

interface CardState {
  box: number; // 1..5 (Leitner — box 5 = mastered)
  reviewCount: number;
  lastReviewed: number;
}

type Direction = 'en-vi' | 'vi-en';
type Rating = 'hard' | 'normal' | 'easy';

interface DeckState {
  cards: Record<string, CardState>;
  totalReviewed: number;
  direction: Direction;
}

const STORAGE_PREFIX = 'eng-toeic.flashcard.';
const MASTERED_BOX = 5;

@Component({
  selector: 'app-vocab-flashcard',
  standalone: true,
  imports: [TooltipTextComponent],
  templateUrl: './vocab-flashcard.component.html',
  styleUrl: './vocab-flashcard.component.scss',
})
export class VocabFlashcardComponent implements OnChanges {
  readonly tts = inject(TtsService);

  @Input({ required: true }) items: FlashcardItem[] = [];
  @Input({ required: true }) deckId = '';

  /** Session deck — non-mastered cards sorted by ascending box. */
  private readonly sessionDeck = signal<FlashcardItem[]>([]);
  private readonly deckState = signal<DeckState>({
    cards: {},
    totalReviewed: 0,
    direction: 'en-vi',
  });

  readonly cardIndex = signal(0);
  readonly revealed = signal(false);
  readonly sessionFinished = signal(false);
  readonly sessionStats = signal({ hard: 0, normal: 0, easy: 0 });

  readonly direction = computed(() => this.deckState().direction);
  readonly currentCard = computed(() => this.sessionDeck()[this.cardIndex()]);
  readonly totalInSession = computed(() => this.sessionDeck().length);
  readonly progressPct = computed(() => {
    const total = this.totalInSession();
    if (!total) return 0;
    return Math.round((this.cardIndex() / total) * 100);
  });

  readonly deckStats = computed(() => {
    const state = this.deckState();
    const total = this.items.length;
    let mastered = 0;
    let learning = 0;
    let fresh = 0;
    for (const item of this.items) {
      const s = state.cards[item.word];
      if (!s) {
        fresh++;
      } else if (s.box >= MASTERED_BOX) {
        mastered++;
      } else {
        learning++;
      }
    }
    return { total, mastered, learning, fresh };
  });

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['deckId'] || changes['items']) {
      this.loadState();
      this.startSession();
    }
  }

  // ─── Session control ─────────────────────────────────────

  startSession(): void {
    this.sessionDeck.set(this.buildSessionDeck());
    this.cardIndex.set(0);
    this.revealed.set(false);
    this.sessionFinished.set(false);
    this.sessionStats.set({ hard: 0, normal: 0, easy: 0 });
  }

  /** Re-shuffle and restart, including mastered cards (for refresher). */
  startFullReview(): void {
    const deck = [...this.items].sort(() => Math.random() - 0.5);
    this.sessionDeck.set(deck);
    this.cardIndex.set(0);
    this.revealed.set(false);
    this.sessionFinished.set(false);
    this.sessionStats.set({ hard: 0, normal: 0, easy: 0 });
  }

  resetProgress(): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem(STORAGE_PREFIX + this.deckId);
    }
    this.deckState.set({
      cards: {},
      totalReviewed: 0,
      direction: this.deckState().direction,
    });
    this.startSession();
  }

  toggleDirection(): void {
    const next = this.direction() === 'en-vi' ? 'vi-en' : 'en-vi';
    this.deckState.update((s) => ({ ...s, direction: next }));
    this.saveState();
    this.revealed.set(false);
  }

  // ─── Card interactions ──────────────────────────────────

  reveal(): void {
    if (this.revealed()) return;
    this.revealed.set(true);
    // Auto-play the English word's audio when answer is revealed
    const card = this.currentCard();
    if (card) this.tts.speak(card.word, 'en-US');
  }

  rate(rating: Rating): void {
    if (!this.revealed()) return;
    const card = this.currentCard();
    if (!card) return;

    this.applyRating(card.word, rating);
    this.sessionStats.update((s) => ({ ...s, [rating]: s[rating] + 1 }));

    const next = this.cardIndex() + 1;
    if (next >= this.totalInSession()) {
      this.sessionFinished.set(true);
    } else {
      this.cardIndex.set(next);
      this.revealed.set(false);
    }
  }

  speakWord(event: Event): void {
    event.stopPropagation();
    const card = this.currentCard();
    if (card) this.tts.speak(card.word, 'en-US');
  }

  // ─── Keyboard shortcuts ─────────────────────────────────

  @HostListener('document:keydown', ['$event'])
  onKeydown(event: KeyboardEvent): void {
    if (this.sessionFinished()) return;
    // Don't hijack typing in inputs/selects
    const target = event.target as HTMLElement | null;
    if (target && ['INPUT', 'TEXTAREA', 'SELECT'].includes(target.tagName)) {
      return;
    }
    if (event.key === ' ' || event.key === 'Enter') {
      event.preventDefault();
      if (!this.revealed()) this.reveal();
    } else if (this.revealed()) {
      if (event.key === '1') this.rate('hard');
      else if (event.key === '2') this.rate('normal');
      else if (event.key === '3') this.rate('easy');
    }
  }

  // ─── Internal: Leitner box updates ──────────────────────

  private applyRating(word: string, rating: Rating): void {
    this.deckState.update((state) => {
      const current = state.cards[word] ?? {
        box: 1,
        reviewCount: 0,
        lastReviewed: 0,
      };
      let nextBox = current.box;
      if (rating === 'hard') nextBox = 1;
      else if (rating === 'easy')
        nextBox = Math.min(MASTERED_BOX, current.box + 1);
      // 'normal' → keep box

      const next = {
        ...state,
        totalReviewed: state.totalReviewed + 1,
        cards: {
          ...state.cards,
          [word]: {
            box: nextBox,
            reviewCount: current.reviewCount + 1,
            lastReviewed: Date.now(),
          },
        },
      };
      return next;
    });
    this.saveState();
  }

  private buildSessionDeck(): FlashcardItem[] {
    const state = this.deckState();
    const nonMastered = this.items.filter((item) => {
      const s = state.cards[item.word];
      return !s || s.box < MASTERED_BOX;
    });
    // Sort: lower box first (more urgent). New cards (no state) get box=1.
    return nonMastered.sort((a, b) => {
      const ba = state.cards[a.word]?.box ?? 1;
      const bb = state.cards[b.word]?.box ?? 1;
      if (ba !== bb) return ba - bb;
      // Same box → oldest reviewed first
      const ta = state.cards[a.word]?.lastReviewed ?? 0;
      const tb = state.cards[b.word]?.lastReviewed ?? 0;
      return ta - tb;
    });
  }

  private loadState(): void {
    if (typeof localStorage === 'undefined' || !this.deckId) return;
    try {
      const raw = localStorage.getItem(STORAGE_PREFIX + this.deckId);
      if (raw) {
        const parsed = JSON.parse(raw) as Partial<DeckState>;
        this.deckState.set({
          cards: parsed.cards ?? {},
          totalReviewed: parsed.totalReviewed ?? 0,
          direction: parsed.direction ?? 'en-vi',
        });
      }
    } catch {
      /* ignore corrupt state */
    }
  }

  private saveState(): void {
    if (typeof localStorage === 'undefined' || !this.deckId) return;
    try {
      localStorage.setItem(
        STORAGE_PREFIX + this.deckId,
        JSON.stringify(this.deckState())
      );
    } catch {
      /* localStorage may be disabled */
    }
  }
}
