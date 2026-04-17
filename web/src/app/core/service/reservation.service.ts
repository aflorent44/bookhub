import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment.development';
import { map, Observable } from 'rxjs';
import { ServiceResponse } from '../type/service-response';
import { AuthService } from './auth-service';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  private http = inject(HttpClient);
  private authService = inject(AuthService);
  private apiUrl: string = environment.apiUrl;

  getReservationsByBookId(bookId: number): Observable<any[]> {
    return this.http.get<ServiceResponse<any[]>>(`${this.apiUrl}/reservation/book/${bookId}`)
      .pipe(map(response => response.data ?? []));
  }

  getReservationsByUserAndBook(userId: number, bookId: number): Observable<any[]> {
    return this.http.get<ServiceResponse<any[]>>(`${this.apiUrl}/reservation/user/${userId}/book/${bookId}`)
      .pipe(map(response => response.data ?? []));
  }

  getMyReservations(): Observable<any[]> {
    return this.http.get<ServiceResponse<any[]>>(`${this.apiUrl}/reservation/my`)
      .pipe(map(response => response.data ?? []));
  }

  reserveBook(bookId: number): Observable<any> {
    const request = { bookId };
    return this.http.post<ServiceResponse<any>>(`${this.apiUrl}/reservation`, request)
      .pipe(map(response => {
        if (response.code !== '9000') {
          throw new Error(response.code);
        }
        return response.data;
      }));
  }

  cancelReservation(reservationId: number): Observable<any> {
    return this.http.delete<ServiceResponse<any>>(`${this.apiUrl}/reservation/${reservationId}`)
      .pipe(map(response => {
        if (response.code !== '9010') {
          throw new Error(response.code);
        }
        return response.data;
      }));
  }

  public getErrorMessage(code: string): string {
    const messages: Record<string, string> = {
      '9001': 'Vous avez atteint votre quota de réservations (5 maximum).',
      '9002': 'Livre introuvable.',
      '9003': 'Vous avez déjà une réservation en attente pour ce livre.',
      '9004': 'Vous avez déjà un emprunt actif pour ce livre.',
      '9011': 'Réservation introuvable.',
      '9012': 'Seules les réservations en attente peuvent être annulées.',
    };
    return messages[code] ?? 'Erreur lors de la réservation.';
  }
}
