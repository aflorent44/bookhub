import { authGuard, publicGuard } from './core/guards/auth-guard';
import {Routes} from '@angular/router';

export const routes: Routes = [
  // Routes publiques (redirige si déjà connecté)
  { path: 'login', canActivate: [publicGuard], loadComponent: () => import('./features/user/login-form/login-form').then(m => m.LoginForm) },
  { path: 'register', canActivate: [publicGuard], loadComponent: () => import('./features/user/registration-form/registration-form').then(m => m.RegistrationForm) },

  // Routes protégées
  { path: 'books', canActivate: [authGuard], loadComponent: () => import('./features/book/book-form/book-form').then(m => m.BookForm) },
  { path: 'books/:id', canActivate: [authGuard], loadComponent: () => import('./features/book/book-detail/book-detail').then(m => m.BookDetail) },

  { path: '', redirectTo: 'login', pathMatch: 'full' }
];
