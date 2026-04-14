import {inject, Injectable, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {tap} from 'rxjs';
import {UserResponse} from '../type/user-response';
import {LoginRequest} from '../type/login-request';
import {AuthResponse} from '../type/auth-response';
import {RegisterRequest} from '../type/register-request';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private http = inject(HttpClient);
  private router = inject(Router);

  private readonly API_URL = 'http://localhost:8080/api/auth';

  currentUser$ = signal<UserResponse | null>(null);

  login(credentials: LoginRequest) {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, credentials).pipe(
      tap(res => {
        localStorage.setItem('token', res.token);
        this.currentUser$.set(res.user);
        this.router.navigate(['/home']);
      })
    );
  }

  register(data: RegisterRequest) {
    return this.http.post<UserResponse>(`${this.API_URL}/register`, data);
  }

  logout() {
    localStorage.removeItem('token');
    this.currentUser$.set(null);
    this.router.navigate(['/login']);
  }

  getToken() {
    return localStorage.getItem('token');
  }
}
