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
  {
    path: 'chu-de/marketing/tu-vung',
    loadComponent: () =>
      import('./pages/marketing/vocab/marketing-vocab.component').then(
        (m) => m.MarketingVocabComponent
      ),
  },
  {
    path: 'chu-de/marketing/doan-van',
    loadComponent: () =>
      import('./pages/marketing/passage/marketing-passage.component').then(
        (m) => m.MarketingPassageComponent
      ),
  },
  {
    path: 'chu-de/warranty/tu-vung',
    loadComponent: () =>
      import('./pages/warranty/vocab/warranty-vocab.component').then(
        (m) => m.WarrantyVocabComponent
      ),
  },
  {
    path: 'chu-de/warranty/doan-van',
    loadComponent: () =>
      import('./pages/warranty/passage/warranty-passage.component').then(
        (m) => m.WarrantyPassageComponent
      ),
  },
  {
    path: 'chu-de/business-planning/tu-vung',
    loadComponent: () =>
      import(
        './pages/business-planning/vocab/business-planning-vocab.component'
      ).then((m) => m.BusinessPlanningVocabComponent),
  },
  {
    path: 'chu-de/business-planning/doan-van',
    loadComponent: () =>
      import(
        './pages/business-planning/passage/business-planning-passage.component'
      ).then((m) => m.BusinessPlanningPassageComponent),
  },
];
