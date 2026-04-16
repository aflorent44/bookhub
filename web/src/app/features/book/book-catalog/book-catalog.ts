import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { PaginatorModule, PaginatorState } from 'primeng/paginator';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { BadgeModule } from 'primeng/badge';
import { BookService } from "../../../core/service/book.service";
import { Book } from "../../../core/type/book";
import { BookFilters, DEFAULT_FILTERS } from "../../../core/type/book-filters";
import { SortOption } from '../../../core/type/sort-option';
import { BookCard } from "./book-card/book-card";
import { CatalogFilters } from "./catalog-filters/catalog-filters";
import { SortField } from "../../../core/type/sort-field";
import { SortDirection } from "../../../core/type/sort-direction";

@Component({
  selector: 'app-book-catalog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    InputTextModule,
    ButtonModule,
    SelectModule,
    PaginatorModule,
    ProgressSpinnerModule,
    BadgeModule,
    BookCard,
    CatalogFilters,
  ],
  templateUrl: './book-catalog.html',
  styleUrl: './book-catalog.scss',
})
export class BookCatalog implements OnInit {
  private readonly bookService = inject(BookService);

  books = signal<Book[]>([]);
  total = signal(0);
  loading = signal(false);
  drawerVisible = signal(false);
  filters = signal<BookFilters>({ ...DEFAULT_FILTERS });
  searchKeyword = signal('');

  readonly sortOptions: SortOption[] = [
    { label: 'Titre (A→Z)', field: SortField.TITLE, direction: SortDirection.ASCENDING },
    { label: 'Titre (Z→A)', field: SortField.TITLE, direction: SortDirection.DESCENDING },
    { label: 'Auteur (A→Z)', field: SortField.AUTHOR, direction: SortDirection.ASCENDING },
    { label: 'Auteur (Z→A)', field: SortField.AUTHOR, direction: SortDirection.DESCENDING },
    { label: 'Année (récent)', field: SortField.YEAR, direction: SortDirection.DESCENDING },
    { label: 'Année (ancien)', field: SortField.YEAR, direction: SortDirection.ASCENDING },
  ];

  readonly sortControl = new FormControl<SortOption | null>(null);

  activeFilterCount = computed(() => {
    const f = this.filters();
    return [
      f.keyword,
      f.authorFirstName,
      f.authorLastName,
      f.genre,
      f.publisher,
      f.year,
      f.countryName,
      f.countryNationality,
    ].filter(v => v !== undefined && v !== null && v !== '').length;
  });

  ngOnInit(): void {
    this.fetchBooks();

    this.sortControl.valueChanges.subscribe(option => {
      this.onSortChange(option);
    });
  }

  private fetchBooks(updated?: Partial<BookFilters>): void {
    this.loading.set(true);

    const nextFilters: BookFilters = updated
      ? { ...this.filters(), ...updated }
      : this.filters();

    this.filters.set(nextFilters);

    this.bookService.getBooks(nextFilters).subscribe({
      next: (response) => {
        this.books.set(response.data?.content ?? response.data ?? []);
        this.total.set(response.data?.totalElements ?? this.books().length);
        this.loading.set(false);
      },
      error: (error) => {
        console.error('Erreur chargement des livres :', error);
        this.loading.set(false);
      },
    });
  }

  onSearch(): void {
    this.fetchBooks({
      keyword: this.searchKeyword(),
      page: 0,
    });
  }

  onFiltersChange(updated: Partial<BookFilters>): void {
    this.fetchBooks({
      ...updated,
      page: 0,
    });
  }

  onSortChange(option: SortOption | null): void {
    this.fetchBooks({
      sortBy: option?.field ?? null,
      sortDirection: option?.direction ?? null,
      page: 0,
    });
  }

  onPageChange(event: PaginatorState): void {
    if (event.page === undefined) return;

    this.fetchBooks({
      page: event.page,
    });
  }

  onKeywordInput(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.searchKeyword.set(target.value);
  }

  openFilters(): void {
    this.drawerVisible.set(true);
  }
}
