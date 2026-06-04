import { Component, computed, inject, signal } from '@angular/core';
import { TtsService } from '../../../services/tts.service';
import { TooltipTextComponent } from '../../../shared/tooltip/tooltip-text.component';
import { IconButtonComponent } from '../../../shared/icon-button/icon-button.component';
import { PageHeadComponent } from '../../../shared/page-head/page-head.component';
import {
  ListenItem,
  VocabListenComponent,
} from '../../../shared/vocab-listen/vocab-listen.component';
import {
  FlashcardItem,
  VocabFlashcardComponent,
} from '../../../shared/vocab-flashcard/vocab-flashcard.component';
import BUSINESS_PLANNING_LESSON_4 from '../../../data/business-planning-lesson-4.json';
import type { VocabEntry } from '../../../data/types';

@Component({
  selector: 'app-business-planning-vocab',
  standalone: true,
  imports: [
    TooltipTextComponent,
    IconButtonComponent,
    PageHeadComponent,
    VocabListenComponent,
    VocabFlashcardComponent,
  ],
  templateUrl: './business-planning-vocab.component.html',
  styleUrl: './business-planning-vocab.component.scss',
})
export class BusinessPlanningVocabComponent {
  readonly tts = inject(TtsService);

  readonly vocab = BUSINESS_PLANNING_LESSON_4 as VocabEntry[];

  readonly activeTab = signal<'vocab' | 'listen' | 'flashcard'>('vocab');

  readonly listenItems = computed<ListenItem[]>(() =>
    this.vocab.map((v) => ({ word: v.word, vi: v.vi }))
  );

  readonly flashcardItems = computed<FlashcardItem[]>(() =>
    this.vocab.map((v) => ({
      word: v.word,
      vi: v.vi,
      ipa: v.ipa,
      example: v.example,
      exampleVi: v.exampleVi,
    }))
  );

  private readonly translated = signal(new Set<string>());

  toggleTranslate(word: string): void {
    const next = new Set(this.translated());
    next.has(word) ? next.delete(word) : next.add(word);
    this.translated.set(next);
  }

  isTranslated(word: string): boolean {
    return this.translated().has(word);
  }
}
