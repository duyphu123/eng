import { Component, Input, computed, inject, signal } from '@angular/core';
import { TooltipService } from './tooltip.service';
import { COMMON_DICT } from '../dict/common-toeic';
import { lookup } from '../dict/lemma';
import autoDictData from '../dict/auto-dict.json';
import autoPhrasesData from '../dict/auto-phrases.json';

const AUTO_DICT: Readonly<Record<string, string | null>> = autoDictData as Record<
  string,
  string | null
>;

const AUTO_PHRASES: Readonly<Record<string, string | null>> =
  autoPhrasesData as Record<string, string | null>;

// Longest n-gram size we attempt during phrase matching.
const MAX_PHRASE_WORDS = 3;

interface Token {
  type: 'text' | 'mark';
  text: string;
  vi?: string;
  isHighlight?: boolean;
  isAuto?: boolean; // matched from COMMON_DICT / AUTO_DICT / AUTO_PHRASES (not manual markup)
}

interface Atom {
  type: 'word' | 'gap';
  text: string;
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

/** Split text into a flat list of word/gap atoms preserving the original spacing. */
function atomize(text: string): Atom[] {
  const atoms: Atom[] = [];
  if (!text) return atoms;
  // Capture group with split keeps the matched word tokens
  const parts = text.split(/(\b[a-zA-Z]+\b)/);
  for (const part of parts) {
    if (!part) continue;
    atoms.push({
      type: /^[a-zA-Z]+$/.test(part) ? 'word' : 'gap',
      text: part,
    });
  }
  return atoms;
}

/** True if every char between two words is plain whitespace — so a phrase
 *  doesn't accidentally span across commas, periods, or other punctuation. */
function isPureWhitespace(s: string): boolean {
  return /^\s+$/.test(s);
}

/**
 * Walk the atom list greedily: at each word position, try a 3-word phrase
 * first, then 2-word, then single word lookup. Emits tokens (text or mark).
 */
function tokenizeAutoMatched(text: string, tokens: Token[]): void {
  const atoms = atomize(text);
  let i = 0;

  while (i < atoms.length) {
    const atom = atoms[i];

    if (atom.type === 'gap') {
      tokens.push({ type: 'text', text: atom.text });
      i++;
      continue;
    }

    // Try longest phrase match first
    let phraseConsumed = 0;
    for (let n = MAX_PHRASE_WORDS; n >= 2; n--) {
      const wordIndices: number[] = [];
      let valid = true;
      let k = i;
      while (wordIndices.length < n && k < atoms.length) {
        const a = atoms[k];
        if (a.type === 'word') {
          wordIndices.push(k);
        } else if (wordIndices.length > 0 && wordIndices.length < n) {
          // Separator between phrase words must be pure whitespace
          if (!isPureWhitespace(a.text)) {
            valid = false;
            break;
          }
        }
        k++;
      }

      if (!valid || wordIndices.length < n) continue;

      const lastWordIdx = wordIndices[n - 1];
      const phraseKey = wordIndices
        .map((idx) => atoms[idx].text.toLowerCase())
        .join(' ');
      const vi = AUTO_PHRASES[phraseKey];
      if (vi) {
        const displayText = atoms
          .slice(i, lastWordIdx + 1)
          .map((a) => a.text)
          .join('');
        tokens.push({ type: 'mark', text: displayText, vi, isAuto: true });
        phraseConsumed = lastWordIdx + 1 - i;
        break;
      }
    }

    if (phraseConsumed > 0) {
      i += phraseConsumed;
      continue;
    }

    // Single word fallback
    const vi = lookup(atom.text, COMMON_DICT, AUTO_DICT);
    if (vi) {
      tokens.push({ type: 'mark', text: atom.text, vi, isAuto: true });
    } else {
      tokens.push({ type: 'text', text: atom.text });
    }
    i++;
  }
}

function parseMarkup(input: string, highlight: string | null): Token[] {
  const tokens: Token[] = [];
  const re = /\[([^\]]+)\]\{([^}]+)\}/g;
  let last = 0;
  let m: RegExpExecArray | null;

  while ((m = re.exec(input)) !== null) {
    if (m.index > last) tokenizeAutoMatched(input.slice(last, m.index), tokens);
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
  if (last < input.length) tokenizeAutoMatched(input.slice(last), tokens);

  return tokens;
}
