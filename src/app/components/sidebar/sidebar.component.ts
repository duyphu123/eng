import { Component, signal } from '@angular/core';
import { NgTemplateOutlet } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';

interface MenuNode {
  label: string;
  route?: string;
  children?: MenuNode[];
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [NgTemplateOutlet, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss',
})
export class SidebarComponent {
  readonly menu: MenuNode[] = [
    {
      label: 'Chủ đề',
      children: [
        {
          label: 'Contract',
          children: [
            { label: 'Từ vựng', route: '/chu-de/contract/tu-vung' },
            { label: 'Đoạn văn', route: '/chu-de/contract/doan-van' },
          ],
        },
      ],
    },
  ];

  private readonly expanded = signal(new Set<string>(['Chủ đề', 'Contract']));

  toggle(label: string): void {
    const next = new Set(this.expanded());
    next.has(label) ? next.delete(label) : next.add(label);
    this.expanded.set(next);
  }

  isExpanded(label: string): boolean {
    return this.expanded().has(label);
  }
}
