import { Component, Input } from '@angular/core';

/**
 * Standard page header — eyebrow + title + subtitle, with border-bottom and
 * responsive padding/typography. Use on every content page for consistency.
 */
@Component({
  selector: 'app-page-head',
  standalone: true,
  template: `
    @if (eyebrow) {
      <p class="eyebrow">{{ eyebrow }}</p>
    }
    <h1>{{ title }}</h1>
    @if (subtitle) {
      <p class="sub">{{ subtitle }}</p>
    }
  `,
  styleUrl: './page-head.component.scss',
})
export class PageHeadComponent {
  @Input() eyebrow?: string;
  @Input({ required: true }) title!: string;
  @Input() subtitle?: string;
}
