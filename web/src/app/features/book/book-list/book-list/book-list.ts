import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Book } from '../../../../core/type/book';

import { BookGrid } from '../book-grid/book-grid';
import { BookFilter } from '../book-filter/book-filter';
import { BookSort, SortDirection, SortField } from '../book-sort/book-sort';

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
  sortBy: SortField;
  sortDirection: SortDirection;
}

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    BookGrid,
    BookFilter,
    BookSort
  ],
  templateUrl: './book-list.html',
  styleUrl: './book-list.scss',
})
export class BookList implements OnInit {

  private allBooks: Book[] = [

  ];

  books: Book[] = [];
  total = 0;

  filters: BookFilters = {
    keyword: '',
    page: 0,
    size: 21,
    sortBy: 'name',
    sortDirection: null
  };

  ngOnInit(): void {
    this.loadBooks();
  }

  loadBooks(): void {
    let filtered = [...this.allBooks];

    if (this.filters.keyword) {
      const keyword = this.filters.keyword.toLowerCase();
      filtered = filtered.filter(book =>
        book.title.toLowerCase().includes(keyword) ||
        book.authorFirstName.toLowerCase().includes(keyword) ||
        book.authorLastName.toLowerCase().includes(keyword)
      );
    }

    if (this.filters.genre) {
      const search = this.filters.genre.toLowerCase();

      filtered = filtered.filter(book =>
        book.genres?.some(g =>
          g.label.toLowerCase().includes(search)
        )
      );
    }

    const start = this.filters.page * this.filters.size;
    const end = start + this.filters.size;

    this.books = filtered.slice(start, end);
    this.total = filtered.length;
  }

  onFiltersChange(updated: Partial<BookFilters>): void {
    this.filters = { ...this.filters, ...updated, page: 0 };
    this.loadBooks();
  }

  onPageChange(page: number): void {
    this.filters.page = page;
    this.loadBooks();
  }

  onSortChange(event: { sortBy: SortField; sortDirection: SortDirection }) {
    this.filters = {
      ...this.filters,
      sortBy: event.sortBy,
      sortDirection: event.sortDirection
    };
    this.loadBooks();
  }

  onSearch(): void {
    this.filters.page = 0;
    this.loadBooks();
  }
}
