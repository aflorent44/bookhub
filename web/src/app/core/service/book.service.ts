import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import { map } from 'rxjs';
import { Book } from '../type/book';
import { ServiceResponse } from '../type/service-response';
import { environment } from '../../../environments/environment.development';
import { BookFilters } from "../type/book-filters";

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private http = inject(HttpClient);
  private apiUrl : string = environment.apiUrl;
  private googleBooksApiUrl : string = environment.googleBooksApiUrl;

  getBookById(id: number): Observable<Book> {
    return this.http.get<ServiceResponse<Book>>(`${this.apiUrl}/books/${id}`)
      .pipe(map(response => response.data));
  }

  getBooks(filters: BookFilters): Observable<any> {
    return this.http.post<ServiceResponse<any>>(
      `${this.apiUrl}/books/search`, filters
    );
  }

  createBook(book: any): Observable<Book> {
    return this.http.post<ServiceResponse<Book>>(`${this.apiUrl}/books`, book)
      .pipe(
        map((response: ServiceResponse<Book>) => {
          if (response.code !== '1030') {
            throw new Error(response.code);
          }
          return response.data;
        }),
        catchError((err) => {
          const code = err?.error?.code ?? err?.message ?? 'UNKNOWN';
          return throwError(() => new Error(code));
        })
      );
  }


  updateBook(book: any): Observable<Book> {
    return this.http.post<ServiceResponse<Book>>(`${this.apiUrl}/books/update`, book)
      .pipe(
        map((response: ServiceResponse<Book>) => {
          if (response.code !== '1033') {
            throw new Error(response.code);
          }
          return response.data;
        }),
        catchError((err) => {
          const code = err?.error?.code ?? err?.message ?? 'UNKNOWN';
          return throwError(() => new Error(code));
        })
      );
  }

  getBookInfoFromGoogleByIsbn(isbn: string): Observable<any> {
    return this.http.get<any>(`${this.googleBooksApiUrl}?q=isbn:${isbn}`);
  }

  public getErrorMessage(err: any): string {
    const code: string = err?.message ?? '';
    const messages: Record<string, string> = {
      '1021': 'Le titre est requis.',
      '1022': 'L\'ISBN est requis.',
      '1023': 'Au moins un genre est requis.',
      '1031': 'Ce livre existe déjà (ISBN déjà enregistré).',
      '1032': 'Utilisateur introuvable.',
      '1011': 'Livre introuvable.',
    };
    return messages[code] ?? 'Erreur lors de la création du livre.';
  }
}
