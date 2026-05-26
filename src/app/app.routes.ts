import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/home/home.component').then((m) => m.HomeComponent),
  },
  {
    path: 'chu-de/contract/tu-vung',
    loadComponent: () =>
      import('./pages/contract/vocab/contract-vocab.component').then(
        (m) => m.ContractVocabComponent
      ),
  },
  {
    path: 'chu-de/contract/doan-van',
    loadComponent: () =>
      import('./pages/contract/passage/contract-passage.component').then(
        (m) => m.ContractPassageComponent
      ),
  },
];
