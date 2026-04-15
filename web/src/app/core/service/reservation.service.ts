import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment.development';
import {map, Observable} from 'rxjs';
import {ServiceResponse} from '../type/service-response';
import {Reservation} from "../type/reservation";

@Injectable({
  providedIn: 'root',
})
export class ReservationService {

  private http = inject(HttpClient);
  private apiUrl: string = environment.apiUrl;

  getReservationsByBookId(bookId: number): Observable<any[]> {
    return this.http.get<ServiceResponse<any[]>>(`${this.apiUrl}/reservation/book/${bookId}`)
      .pipe(map(response => {
        if (response.code !== '9021') {
          return [];
        }
        return response.data;
      }));
  }

  getReservationsByUserAndBook(userId: number, bookId: number): Observable<any[]> {
    return this.http.get<ServiceResponse<any[]>>(`${this.apiUrl}/reservation/user/${userId}/book/${bookId}`)
      .pipe(map(response => {
        if (response.code !== '9031') return [];
        return response.data;
      }));
  }

  reserveBook(bookId: number): Observable<Reservation> {
    const request = {bookId};

    return this.http
      .post<ServiceResponse<Reservation>>(`${this.apiUrl}/reservation`, request)
      .pipe(
        map((response) => {
          if (response.code !== '9000') {
            throw new Error(response.code);
          }
          return response.data;
        })
      );
  }

  cancelReservation(reservationId: number): Observable<any> {
    return this.http.post<ServiceResponse<any>>(`${this.apiUrl}/reservation/${reservationId}`, {})
      .pipe(map(response => {
        if (response.code !== '9010') {
          throw new Error(response.code);
        }
        return response.data;
      }));
  }

  public getErrorMessage(code: string): string {
    const messages: Record<string, string> = {
      '8001': 'Utilisateur introuvable.',
      '9001': 'Vous avez atteint votre quota de réservations (5 maximum).',
      '9002': 'Livre introuvable.',
      '9011': 'Réservation introuvable.',
      '9012': 'Seules les réservations en attente peuvent être annulées.',
      '9003': 'Vous avez déjà une réservation en attente pour ce livre.',
      '9004': 'Vous avez déjà un emprunt actif pour ce livre.',
    };
    return messages[code] ?? 'Erreur lors de la réservation.';
  }


}
