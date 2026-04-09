import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: 'book', loadComponent: () => import('./features/book/book-form/book-form').then(m => m.BookForm), title: 'Ajouter un livre' },
];
