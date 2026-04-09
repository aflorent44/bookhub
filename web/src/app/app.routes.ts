import {Routes} from '@angular/router';

export const routes: Routes = [
  {
    path: 'register',
    loadComponent: () => import('./features/user/registration-form/registration-form')
      .then(m => m.RegistrationForm),
    title: 'Inscription - Bookhub'
  },

];
