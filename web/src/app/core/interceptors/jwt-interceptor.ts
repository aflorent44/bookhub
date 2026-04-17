import { HttpInterceptorFn } from '@angular/common/http';
import {inject} from '@angular/core';
import {AuthService} from '../service/auth-service';
import { environment } from "../../../environments/environment.development";

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  // pour ne pas bloquer la requete api google
  const isBackendCall = req.url.startsWith(environment.apiUrl);

  if (token && isBackendCall) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }
  return next(req);
};
