import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: 'books', loadComponent: () => import('./features/book/book-form/book-form').then(m => m.BookForm), title: 'Ajouter un livre' },

  { path: 'books/:id', loadComponent: () => import('./features/book/book-detail/book-detail').then(m => m.BookDetail), title: 'Afficher un livre' },

  { path: 'register',
     loadComponent: () => import('./features/user/registration-form/registration-form')
      .then(m => m.RegistrationForm),
    title: 'Inscription - Bookhub'
  },

  { path: 'login', loadComponent: () => import('./features/user/login-form/login-form')
      .then(m => m.LoginForm),
  title: 'Connexion - Bookhub'
  },

  { path: '', redirectTo: 'login', pathMatch: 'full' }
];
