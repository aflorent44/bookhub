import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserResponse} from '../type/user-response';
import {UpdateProfileRequest} from '../type/update-profile-request';
import {Reservation} from "../type/reservation";
import {environment} from "../../../environments/environment.development";
import {ServiceResponse} from "../type/service-response";
import {map} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;

  getProfile(): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.apiUrl}/users/me`);
  }

  updateProfile(request: UpdateProfileRequest): Observable<UserResponse> {
    return this.http.put<UserResponse>(`${this.apiUrl}/users/me`, request);
  }

  changePassword(payload: { currentPassword: string; newPassword: string }) {
    return this.http.put<void>(`${this.apiUrl}/users/me/password`, payload);
  }

  deleteAccount(): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/users/me`);
  }

  getMyReservations(): Observable<Reservation[]> {
    return this.http
      .get<ServiceResponse<Reservation[]>>(`${this.apiUrl}/reservation/my`)
      .pipe(map((res) => res.data ?? []));
  }

  cancelReservation(reservationId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/reservation/${reservationId}`);
  }
}
