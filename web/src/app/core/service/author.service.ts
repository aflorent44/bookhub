import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment.development';
import { Observable } from 'rxjs';
import { Author } from '../types/author';

@Injectable({ providedIn: 'root' })
export class AuthorService {
  private http = inject(HttpClient);

  private apiUrl = environment.apiUrl;

  createAuthor(author: Partial<Author>): Observable<Author> {
    return this.http.post<Author>(`${this.apiUrl}/authors`, author);
  }

  getAuthors(): Observable<Author[]> {
    return this.http.get<Author[]>(`${this.apiUrl}/authors`);
  }

  getAuthorById(id: number): Observable<Author> {
    return this.http.get<Author>(`${this.apiUrl}/authors/${id}`);
  }
}
