import {inject, Injectable, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {map, Observable, tap} from 'rxjs';
import {UserResponse} from '../type/user-response';
import {LoginRequest} from '../type/login-request';
import {AuthResponse} from '../type/auth-response';
import {RegisterRequest} from '../type/register-request';
import {environment} from '../../../environments/environment.development';
import {User} from '../type/user';
import {ServiceResponse} from '../type/service-response';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private http = inject(HttpClient);
  private router = inject(Router);

  private readonly API_URL = environment.apiUrl;

  currentUser$ = signal<UserResponse | null>(this.getUserFromStorage());

  private getUserFromStorage(): UserResponse | null {
    const token = localStorage.getItem('token');
    const user = localStorage.getItem('user');
    if (token && user) {
      return JSON.parse(user);
    }
    return null;
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<ServiceResponse<User>>(`${this.API_URL}/users/${id}`)
      .pipe(map(response => response.data));
  }

  login(credentials: LoginRequest) {
    return this.http.post<AuthResponse>(`${this.API_URL}/auth/login`, credentials).pipe(
      tap(res => {
        localStorage.setItem('token', res.token);
        this.currentUser$.set(res.user);
        this.router.navigate(['/home']);
      })
    );
  }

  register(data: RegisterRequest) {
    return this.http.post<UserResponse>(`${this.API_URL}/auth/register`, data);
  }

  logout() {
    localStorage.clear();
    this.currentUser$.set(null);
    this.router.navigate(['/login']);
  }

  getToken() {
    return localStorage.getItem('token');
  }
}
