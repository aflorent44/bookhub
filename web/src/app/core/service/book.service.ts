import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from '../type/book';
import { of } from 'rxjs';
import {Author} from '../type/author';
import {Publisher} from '../type/publisher';
import {Country} from '../type/country';

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private apiUrl = 'http://localhost:3000/api'; // à adapter

  constructor(private http: HttpClient) {}

  getBookById(id: string): Observable<Book> {

    const mockPublisher: Partial<Publisher> = {
      id: 1,
      name: "Penguin Books"
    };

    const mockCountry: Partial<Country> = {
      id: 1,
      code: "GBR",
      name: "United Kingdom",
      nationality: "British"
    };

    const mockAuthor: Partial<Author> = {
      id: 1,
      firstName: "Jane",
      lastName: "Austen",
      country: mockCountry as Country
    };

  const mockBook: Partial<Book> = {
  id: 2,
  isbn: "9780140430721",
  title: "Pride and Prejudice",
  year: 1813,
  quantity: 3,
  description: "A classic novel on manners and marriage.",
  first_page_url: "http://books.google.com/books/content?id=viRcjwEACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api",
  createdBy: 1,
  createdAt: new Date ("2026-04-09T16:16:59.73"),
  updatedBy: 1,
  updatedAt: new Date("2026-04-09T16:16:59.73"),
  genres: [
    { id: 2, label: "Classics" },
    { id: 1, label: "Fiction" }
  ],
    author : mockAuthor as Author,
    publisher: mockPublisher as Publisher
};

    return of(mockBook as Book);
    //return this.http.get<Book>(`${this.apiUrl}/books/${id}`);
  }
}
