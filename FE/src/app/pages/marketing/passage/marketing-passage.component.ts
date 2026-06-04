import { Component, computed, inject, signal } from '@angular/core';
import { PageHeadComponent } from '../../../shared/page-head/page-head.component';
import { TooltipTextComponent } from '../../../shared/tooltip/tooltip-text.component';
import { TtsService } from '../../../services/tts.service';
import passageData from '../../../data/marketing-lesson-2-passage.json';

interface Sentence {
  en: string;
  vi: string;
}

@Component({
  selector: 'app-marketing-passage',
  standalone: true,
  imports: [PageHeadComponent, TooltipTextComponent],
  templateUrl: './marketing-passage.component.html',
  styleUrl: './marketing-passage.component.scss',
})
export class MarketingPassageComponent {
  readonly tts = inject(TtsService);
  readonly passage = passageData;
  readonly sentences = passageData.sentences as Sentence[];

  readonly showVi = signal(false);

  // The plain English string used as audio manifest key — must match exactly
  // what the gen-audio script produced: stripped markup, sentences joined.
  private readonly plainEn = this.sentences
    .map((s) => s.en.replace(/\[([^\]]+)\]\{[^}]+\}/g, '$1'))
    .join(' ');

  readonly isPlaying = computed(() => this.tts.isSpeaking(this.plainEn));

  togglePlay(): void {
    if (this.isPlaying()) {
      this.tts.stop();
    } else {
      this.tts.speak(this.plainEn, 'en-US');
    }
  }

  toggleVi(): void {
    this.showVi.update((v) => !v);
  }
}
