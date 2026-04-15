import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment.development';
import { map, Observable } from 'rxjs';
import { ServiceResponse } from '../type/service-response';
import { AuthService } from './auth-service';
import { Loan } from '../type/loan';

@Injectable({
  providedIn: 'root',
})
export class LoanService {
  private http = inject(HttpClient);
  private authService = inject(AuthService);
  private apiUrl: string = environment.apiUrl;

  loanBook(bookId: number): Observable<Loan> {
    const user = this.authService.currentUser$();

    if (!user?.id) {
      throw new Error('Utilisateur non identifié');
    }

    const request = {
      userId: user.id,
      bookId: bookId,
    };

    console.log('loanBook() request =', request);

    return this.http.post<ServiceResponse<Loan>>(`${this.apiUrl}/loan`, request)
      .pipe(map(response => {
        if (response.code !== '7000') {
          throw new Error(response.code);
        }
        return response.data;
      }));
  }

  affectLoanToUser(bookId: number, userId: number): Observable<Loan> {
    const internalUserId = this.authService.currentUser$()?.id;

    if (!this.authService.currentUser$()) {
      throw new Error('Utilisateur non identifié');
    }

    if (this.authService.currentUser$()?.role !== 'ADMIN' && this.authService.currentUser$()?.role !== 'LIBRARIAN') {
      throw new Error('Utilisateur non identifié');
    }

    const request = {
      userId: userId,
      internalUserId: internalUserId,
      bookId: bookId,
    };

    return this.http.post<ServiceResponse<Loan>>(`${this.apiUrl}/loan`, request)
      .pipe(map(response => {
        if (response.code !== '7000') {
          throw new Error(response.code);
        }
        return response.data;
      }));
  }

  getLoansByBookId(bookId: number): Observable<Loan[]> {
    return this.http.get<ServiceResponse<Loan[]>>(`${this.apiUrl}/loan/${bookId}`)
      .pipe(map(response => response.data));
  }

  getLoansByUserAndBook(userId: number, bookId: number): Observable<Loan[]> {
    return this.http.get<ServiceResponse<Loan[]>>(`${this.apiUrl}/loan/user/${userId}/book/${bookId}`)
      .pipe(map(response => {
        if (response.code !== '7041') return [];
        return response.data;
      }));
  }

  validateLoan(loanId: number): Observable<any> {
    const internalUserId = this.authService.currentUser$()?.id;

    const request = {
      loanId: loanId,
      internalUserId: internalUserId  // manquant — Java le requiert
    };

    return this.http.post<ServiceResponse<any>>(`${this.apiUrl}/loan/validate`, request)
      .pipe(map(response => {
        if (response.code !== '7020') {  // ← était '7005', doit être '7020'
          throw new Error(response.code);
        }
        return response.data;
      }));
  }

  returnBook(loanId: number, userId: number, bookId: number): Observable<any> {
    const internalUserId = this.authService.currentUser$()?.id;

    const request = {
      loanId: loanId,
      userId: userId,
      bookId: bookId,
      internalUserId: internalUserId
    };

    return this.http.post<ServiceResponse<any>>(`${this.apiUrl}/loan/return`, request)
      .pipe(map(response => {
        if (response.code !== '7010') {
          throw new Error(response.code);
        }
        return response.data;
      }));
  }

  public getErrorMessage(code: string): string {
    const messages: Record<string, string> = {
      '7001': 'Ce livre n\'est plus disponible.',
      '7002': 'Livre introuvable.',
      '7003': 'Vous avez un ou plusieurs livres en retard.',
      '7004': 'Vous avez atteint votre quota d\'emprunts.',
      '7011': 'Emprunt non trouvé',
      '7012':	'Livre introuvable'
    };
    return messages[code] ?? 'Erreur lors de l\'emprunt.';
  }


}
