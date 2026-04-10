import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: 'book', loadComponent: () => import('./features/book/book-form/book-form').then(m => m.BookForm), title: 'Ajouter un livre' },

  {  path: 'register',
     loadComponent: () => import('./features/user/registration-form/registration-form')
      .then(m => m.RegistrationForm),
    title: 'Inscription - Bookhub'
  },

];
