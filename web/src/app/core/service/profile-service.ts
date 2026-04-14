import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserResponse } from '../type/user-response';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `http://localhost:8080/api/users`;

  getProfile(): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.apiUrl}/me`);
  }

  // TODO : UPDATE
  // updateProfile(request: UpdateProfileRequest): Observable<User> {
  //   return this.http.put<User>(`${this.apiUrl}/me`, request);
  // }

  // TODO : change password
  // changePassword(request: ChangePasswordRequest): Observable<void> {
  //   return this.http.put<void>(`${this.apiUrl}/me/password`, request);
  // }

  deleteAccount(): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/me`);
  }
}
