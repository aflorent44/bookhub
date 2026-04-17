import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {AuthService} from '../service/auth-service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.currentUser$() || authService.getToken()) {
    return true;
  }

  router.navigate(['/login']);
  return false;
};

const publicGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.currentUser$() || authService.getToken()) {
    router.navigate(['/catalog']);
    return false;
  }

  return true;
};
export default publicGuard

