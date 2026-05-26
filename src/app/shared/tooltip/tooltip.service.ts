import {
  Injectable,
  NgZone,
  RendererFactory2,
  inject,
  signal,
} from '@angular/core';
import {
  arrow,
  autoUpdate,
  computePosition,
  flip,
  offset,
  shift,
} from '@floating-ui/dom';

/**
 * Global single-tooltip service. Use one popover instance shared across the
 * app — only one tooltip can be visible at a time, which is what users expect.
 */
@Injectable({ providedIn: 'root' })
export class TooltipService {
  private readonly zone = inject(NgZone);
  private readonly rendererFactory = inject(RendererFactory2);
  private readonly renderer = this.rendererFactory.createRenderer(null, null);

  readonly isTouch =
    typeof window !== 'undefined' &&
    window.matchMedia('(hover: none) and (pointer: coarse)').matches;

  readonly visibleAnchor = signal<HTMLElement | null>(null);

  private tooltipEl: HTMLDivElement | null = null;
  private arrowEl: HTMLDivElement | null = null;
  private cleanupAutoUpdate: (() => void) | null = null;
  private currentAnchor: HTMLElement | null = null;
  private outsideHandler: ((e: Event) => void) | null = null;
  private keyHandler: ((e: KeyboardEvent) => void) | null = null;

  show(anchor: HTMLElement, content: string): void {
    if (typeof document === 'undefined') return;

    this.ensureEl();
    if (!this.tooltipEl) return;

    if (this.currentAnchor === anchor) return; // already shown
    this.currentAnchor = anchor;
    this.tooltipEl.textContent = content;
    if (this.arrowEl) this.tooltipEl.appendChild(this.arrowEl);
    this.tooltipEl.dataset['open'] = 'true';
    this.visibleAnchor.set(anchor);

    this.cleanupAutoUpdate?.();
    this.cleanupAutoUpdate = autoUpdate(anchor, this.tooltipEl, () =>
      this.position(anchor)
    );

    this.attachDismissHandlers();
  }

  hide(anchor?: HTMLElement): void {
    if (anchor && this.currentAnchor !== anchor) return;
    if (!this.tooltipEl) return;
    this.tooltipEl.dataset['open'] = 'false';
    this.visibleAnchor.set(null);
    this.currentAnchor = null;
    this.cleanupAutoUpdate?.();
    this.cleanupAutoUpdate = null;
    this.detachDismissHandlers();
  }

  toggle(anchor: HTMLElement, content: string): void {
    if (this.currentAnchor === anchor) {
      this.hide(anchor);
    } else {
      this.show(anchor, content);
    }
  }

  private ensureEl(): void {
    if (this.tooltipEl) return;
    const el = this.renderer.createElement('div') as HTMLDivElement;
    el.className = 'word-tooltip';
    el.setAttribute('role', 'tooltip');
    el.dataset['open'] = 'false';

    const arrowEl = this.renderer.createElement('div') as HTMLDivElement;
    arrowEl.className = 'word-tooltip__arrow';
    el.appendChild(arrowEl);

    document.body.appendChild(el);
    this.tooltipEl = el;
    this.arrowEl = arrowEl;
  }

  private async position(anchor: HTMLElement): Promise<void> {
    if (!this.tooltipEl || !this.arrowEl) return;
    const { x, y, placement, middlewareData } = await computePosition(
      anchor,
      this.tooltipEl,
      {
        placement: 'top',
        middleware: [
          offset(8),
          flip(),
          shift({ padding: 8 }),
          arrow({ element: this.arrowEl }),
        ],
      }
    );

    Object.assign(this.tooltipEl.style, {
      left: `${x}px`,
      top: `${y}px`,
    });

    const side = placement.split('-')[0];
    const oppositeSide =
      ({ top: 'bottom', bottom: 'top', left: 'right', right: 'left' } as const)[
        side as 'top' | 'bottom' | 'left' | 'right'
      ];

    if (middlewareData.arrow) {
      const { x: ax, y: ay } = middlewareData.arrow;
      Object.assign(this.arrowEl.style, {
        left: ax != null ? `${ax}px` : '',
        top: ay != null ? `${ay}px` : '',
        right: '',
        bottom: '',
        [oppositeSide]: '-4px',
      });
    }
  }

  private attachDismissHandlers(): void {
    this.detachDismissHandlers();

    this.outsideHandler = (e: Event) => {
      const t = e.target as Node;
      if (
        this.currentAnchor &&
        !this.currentAnchor.contains(t) &&
        this.tooltipEl &&
        !this.tooltipEl.contains(t)
      ) {
        this.zone.run(() => this.hide());
      }
    };
    this.keyHandler = (e: KeyboardEvent) => {
      if (e.key === 'Escape') this.zone.run(() => this.hide());
    };

    document.addEventListener('pointerdown', this.outsideHandler, true);
    document.addEventListener('keydown', this.keyHandler);
  }

  private detachDismissHandlers(): void {
    if (this.outsideHandler) {
      document.removeEventListener('pointerdown', this.outsideHandler, true);
      this.outsideHandler = null;
    }
    if (this.keyHandler) {
      document.removeEventListener('keydown', this.keyHandler);
      this.keyHandler = null;
    }
  }
}
