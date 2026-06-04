import { Component, computed, inject, signal } from '@angular/core';
import { PageHeadComponent } from '../../../shared/page-head/page-head.component';
import { TooltipTextComponent } from '../../../shared/tooltip/tooltip-text.component';
import { TtsService } from '../../../services/tts.service';
import passageData from '../../../data/business-planning-lesson-4-passage.json';

interface Sentence {
  en: string;
  vi: string;
}

@Component({
  selector: 'app-business-planning-passage',
  standalone: true,
  imports: [PageHeadComponent, TooltipTextComponent],
  templateUrl: './business-planning-passage.component.html',
  styleUrl: './business-planning-passage.component.scss',
})
export class BusinessPlanningPassageComponent {
  readonly tts = inject(TtsService);
  readonly passage = passageData;
  readonly sentences = passageData.sentences as Sentence[];

  readonly showVi = signal(false);

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
