import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs';
import { Book } from '../type/book';
import { ServiceResponse } from '../type/service-response';
import { environment } from '../../../environments/environment.development';

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

  getBooks(): Observable<Book[]> {
    return this.http.get<ServiceResponse<Book[]>>(`${this.apiUrl}/books`)
      .pipe(map((response: ServiceResponse<Book[]>) => response.data));
  }

  createBook(book: any): Observable<Book> {
    return this.http.post<ServiceResponse<Book>>(`${this.apiUrl}/books`, book)
      .pipe(map((response: ServiceResponse<Book>) => {
        console.log('Réponse createBook :', response); // debug
        if (response.code !== '1030') {
          throw new Error(response.code);
        }
        return response.data;
      }));
  }

  getBookInfoFromGoogleByIsbn(isbn: string): Observable<any> {
    return this.http.get<any>(`${this.googleBooksApiUrl}?q=isbn:${isbn}`);
  }
}
