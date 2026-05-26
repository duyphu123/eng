import { Component, Input } from '@angular/core';

/**
 * Square icon button — 36×36 on mobile, 40×40 ≥640px. Project everywhere
 * an icon-only action is needed (speaker, translate, close, more...).
 *
 * - `active` — solid primary fill (e.g. "đang chọn" / "đang phát")
 * - `pulse` — animated halo (combine with `active` for "đang chạy")
 */
@Component({
  selector: 'app-icon-button',
  standalone: true,
  template: `
    <button
      type="button"
      class="icon-btn"
      [class.is-active]="active"
      [class.is-pulse]="pulse"
      [attr.aria-label]="ariaLabel"
      [attr.title]="hoverTitle"
    >
      <ng-content />
    </button>
  `,
  styleUrl: './icon-button.component.scss',
})
export class IconButtonComponent {
  @Input() active = false;
  @Input() pulse = false;
  @Input() ariaLabel?: string;
  @Input() hoverTitle?: string;
}
