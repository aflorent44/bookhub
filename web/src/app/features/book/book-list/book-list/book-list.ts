import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {Book} from '../../../../core/type/book';
import {BookService} from '../../../../core/service/book.service';
import {SortDirection, SortField} from '../book-sort/book-sort';
import { BookGrid } from "../book-grid/book-grid";
import { BookFilter } from '../book-filter/book-filter';
import { BookSort } from '../book-sort/book-sort';

export interface BookFilters {
  keyword?: string;
  title?: string;
  year?: number;
  isbn?: string;
  quantity?: string;
  authorFirstName?: string;
  authorLastName?: string;
  genre?: string;
  publisher?: string;
  countryName?: string;
  countryNationality?: string;
  page: number;
  size: number;
  sortBy: SortField | null;
  sortDirection: SortDirection;
}

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [CommonModule, FormsModule, BookGrid, BookFilter, BookSort],
  templateUrl: './book-list.html',
  styleUrl: './book-list.scss',
})
export class BookList implements OnInit {
  private bookService = inject(BookService);

  books: Book[] = [];
  allBooks: Book[] = [];
  total = 0;
  loading = false;

  filters: BookFilters = {
    keyword: '',
    page: 0,
    size: 21,
    sortBy: 'title',
    sortDirection: null
  };

  ngOnInit(): void {
    this.fetchBooks();
  }

  fetchBooks(): void {
    this.loading = true;
    this.bookService.getBooks(this.filters).subscribe({
      next: (response) => {
        this.books = response.data?.content ?? response.data ?? [];
        this.total = response.data?.totalElements ?? this.books.length;
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur chargement livres :', err);
        this.loading = false;
      }
    });
  }

  onFiltersChange(updated: Partial<BookFilters>): void {
    this.filters = {...this.filters, ...updated, page: 0};
    this.fetchBooks();
  }

  onPageChange(page: number): void {
    this.filters = {...this.filters, page};
    this.fetchBooks();
  }

  onSortChange(event: { sortBy: SortField | null; sortDirection: SortDirection }): void {
    this.filters = {...this.filters, ...event, page: 0};
    this.fetchBooks();
  }

  onSearch(): void {
    this.filters = {...this.filters, page: 0};
    this.fetchBooks();
  }
}
