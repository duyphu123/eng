import { Component, Input, computed, inject, signal } from '@angular/core';
import { TooltipService } from './tooltip.service';
import { COMMON_DICT } from '../dict/common-toeic';
import { lookup } from '../dict/lemma';
import autoDictData from '../dict/auto-dict.json';

const AUTO_DICT: Readonly<Record<string, string | null>> = autoDictData as Record<
  string,
  string | null
>;

interface Token {
  type: 'text' | 'mark';
  text: string;
  vi?: string;
  isHighlight?: boolean;
  isAuto?: boolean; // matched from COMMON_DICT (not manual markup)
}

/**
 * Render an English sentence with inline Vietnamese tooltips. Hybrid model:
 *
 *  1. **Manual markup** `[phrase]{vi}` always wins — for phrasal verbs,
 *     idioms, collocations, and any case the author wants to control.
 *  2. **Auto-match** falls back on `COMMON_DICT` for unmarked single words
 *     (with lemma support for plurals/tenses).
 *
 * Auto-matched words get a subtler dashed underline; manual marks get a
 * solid dotted underline; the `highlight` word (target vocab) gets a strong
 * amber `<mark>`-style fill on top of the manual mark.
 */
@Component({
  selector: 'app-tooltip-text',
  standalone: true,
  template: `
    @for (tok of tokens(); track $index) {
      @if (tok.type === 'mark') {
        <span
          class="wt-mark"
          [class.wt-mark--highlight]="tok.isHighlight"
          [class.wt-mark--auto]="tok.isAuto"
          tabindex="0"
          role="button"
          [attr.aria-label]="tok.text + ': ' + tok.vi"
          (mouseenter)="onEnter($event, tok.vi!)"
          (mouseleave)="onLeave($event)"
          (focus)="onEnter($event, tok.vi!)"
          (blur)="onLeave($event)"
          (click)="onClick($event, tok.vi!)"
          (keydown.enter)="onClick($event, tok.vi!)"
          (keydown.space)="onClick($event, tok.vi!)"
          >{{ tok.text }}</span
        >
      } @else {
        <span>{{ tok.text }}</span>
      }
    }
  `,
  styleUrl: './tooltip-text.component.scss',
})
export class TooltipTextComponent {
  private readonly tooltip = inject(TooltipService);
  private readonly rawText = signal('');
  private readonly highlightWord = signal<string | null>(null);

  @Input({ required: true }) set text(value: string) {
    this.rawText.set(value ?? '');
  }
  @Input() set highlight(value: string | null | undefined) {
    this.highlightWord.set(value ?? null);
  }

  readonly tokens = computed<Token[]>(() =>
    parseMarkup(this.rawText(), this.highlightWord())
  );

  onEnter(e: Event, vi: string): void {
    if (this.tooltip.isTouch) return;
    this.tooltip.show(e.currentTarget as HTMLElement, vi);
  }

  onLeave(e: Event): void {
    if (this.tooltip.isTouch) return;
    this.tooltip.hide(e.currentTarget as HTMLElement);
  }

  onClick(e: Event, vi: string): void {
    e.preventDefault();
    e.stopPropagation();
    this.tooltip.toggle(e.currentTarget as HTMLElement, vi);
  }
}

function parseMarkup(input: string, highlight: string | null): Token[] {
  const tokens: Token[] = [];
  const re = /\[([^\]]+)\]\{([^}]+)\}/g;
  let last = 0;
  let m: RegExpExecArray | null;

  const handleText = (text: string) => {
    // Split keeping word boundaries — capture group preserves matched parts
    const parts = text.split(/(\b[a-zA-Z]+\b)/);
    for (const part of parts) {
      if (!part) continue;
      if (/^[a-zA-Z]+$/.test(part)) {
        // Manual COMMON_DICT wins; AUTO_DICT (Claude-generated) fills the gap
        const vi = lookup(part, COMMON_DICT, AUTO_DICT);
        if (vi) {
          tokens.push({ type: 'mark', text: part, vi, isAuto: true });
        } else {
          tokens.push({ type: 'text', text: part });
        }
      } else {
        tokens.push({ type: 'text', text: part });
      }
    }
  };

  while ((m = re.exec(input)) !== null) {
    if (m.index > last) handleText(input.slice(last, m.index));
    const phrase = m[1];
    tokens.push({
      type: 'mark',
      text: phrase,
      vi: m[2],
      isHighlight:
        !!highlight && phrase.toLowerCase().includes(highlight.toLowerCase()),
    });
    last = re.lastIndex;
  }
  if (last < input.length) handleText(input.slice(last));

  return tokens;
}
