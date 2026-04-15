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

  // ✅ REAL BOOK DATA
  private allBooks: Book[] = [
    {
      id: 1,
      isbn: '9780132350884',
      title: 'Clean Code',
      year: 2008,
      quantity: 5,
      description: 'A handbook of agile software craftsmanship.',
      authorFirstName: 'Robert',
      authorLastName: 'Martin',
      publisherName: 'Prentice Hall',
      firstPageUrl: "https://books.google.com/books/content?id=hjEFCAAAQBAJ&printsec=frontcover&img=2&zoom=2&source=gbs_api",
      genres: [{ id: 1, label: 'Programming' }]
    },
    {
      id: 2,
      isbn: '9780201616224',
      title: 'The Pragmatic Programmer',
      year: 1999,
      quantity: 4,
      description: 'Journey to mastery in software development.',
      authorFirstName: 'Andrew',
      authorLastName: 'Hunt',
      publisherName: 'Addison-Wesley',
      firstPageUrl: "https://pictures.abebooks.com/isbn/9780201616224-fr.jpg",
      genres: [{ id: 1, label: 'Programming' }]
    },
    {
      id: 3,
      isbn: '9780735211292',
      title: 'Atomic Habits',
      year: 2018,
      quantity: 10,
      description: 'An easy & proven way to build good habits and break bad ones.',
      authorFirstName: 'James',
      authorLastName: 'Clear',
      publisherName: 'Avery',
      firstPageUrl: "https://books.google.com/books/content?id=XfFvDwAAQBAJ&printsec=frontcover&img=1&zoom=2&source=gbs_api",
      genres: [{ id: 2, label: 'Self-help' }]
    },
    {
      id: 4,
      isbn: '9780451524935',
      title: '1984',
      year: 1949,
      quantity: 6,
      description: 'A dystopian novel about totalitarian surveillance.',
      authorFirstName: 'George',
      authorLastName: 'Orwell',
      publisherName: 'Secker & Warburg',
      firstPageUrl: "http://books.google.com/books/content?id=Dd9N0AEACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api",
      genres: [{ id: 3, label: 'Dystopian' }]
    },
    {
      id: 5,
      isbn: '9780547928227',
      title: 'The Hobbit',
      year: 1937,
      quantity: 8,
      description: 'A fantasy adventure of Bilbo Baggins.',
      authorFirstName: 'J.R.R.',
      authorLastName: 'Tolkien',
      publisherName: 'George Allen & Unwin',
      firstPageUrl: "http://books.google.com/books/content?id=LLSpngEACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api",
      genres: [{ id: 4, label: 'Fantasy' }]
    },
    {
      id: 6,
      isbn: '9780590353427',
      title: "Harry Potter and the Sorcerer's Stone",
      year: 1997,
      quantity: 12,
      description: 'A young wizard begins his magical journey.',
      authorFirstName: 'J.K.',
      authorLastName: 'Rowling',
      publisherName: 'Bloomsbury',
      firstPageUrl: "http://books.google.com/books/content?id=fo4rzdaHDAwC&printsec=frontcover&img=1&zoom=1&source=gbs_api",
      genres: [{ id: 4, label: 'Fantasy' }]
    },
    {
      id: 7,
      isbn: '9780618640157',
      title: 'The Lord of the Rings',
      year: 1954,
      quantity: 7,
      description: 'An epic quest to destroy the One Ring.',
      authorFirstName: 'J.R.R.',
      authorLastName: 'Tolkien',
      publisherName: 'George Allen & Unwin',
      firstPageUrl: "https://books.google.com/books/content?id=XApOPwAACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api",
      genres: [{ id: 4, label: 'Fantasy' }]
    },
    {
      id: 8,
      isbn: '9780061120084',
      title: 'To Kill a Mockingbird',
      year: 1960,
      quantity: 9,
      description: 'A story of racial injustice in the Deep South.',
      authorFirstName: 'Harper',
      authorLastName: 'Lee',
      publisherName: 'J.B. Lippincott & Co.',
      firstPageUrl: "http://books.google.com/books/content?id=ncuX8p2xLIUC&printsec=frontcover&img=1&zoom=1&source=gbs_api",
      genres: [{ id: 5, label: 'Classic' }]
    },
    {
      id: 9,
      isbn: '9780743273565',
      title: 'The Great Gatsby',
      year: 1925,
      quantity: 6,
      description: 'A critique of the American Dream.',
      authorFirstName: 'F. Scott',
      authorLastName: 'Fitzgerald',
      publisherName: 'Charles Scribner\'s Sons',
      firstPageUrl: "https://m.media-amazon.com/images/I/81T4dS6IkaL.jpg",
      genres: [{ id: 5, label: 'Classic' }]
    },
    {
      id: 10,
      isbn: '9780062316097',
      title: 'Sapiens',
      year: 2011,
      quantity: 11,
      description: 'A brief history of humankind.',
      authorFirstName: 'Yuval',
      authorLastName: 'Harari',
      publisherName: 'Harvill Secker',
      firstPageUrl: "http://books.google.com/books/content?id=ibALnwEACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api",
      genres: [{ id: 6, label: 'History' }]
    },
    {
      id: 11,
      isbn: '9780061122415',
      title: 'The Alchemist',
      year: 1988,
      quantity: 10,
      description: 'A journey of following one’s dreams.',
      authorFirstName: 'Paulo',
      authorLastName: 'Coelho',
      publisherName: 'HarperOne',
      firstPageUrl: "http://books.google.com/books/content?id=pTr44Sx6oWQC&printsec=frontcover&img=1&zoom=2&source=gbs_api",
      genres: [{ id: 7, label: 'Fiction' }]
    },
    {
      id: 12,
      isbn: '9781612680194',
      title: 'Rich Dad Poor Dad',
      year: 1997,
      quantity: 8,
      description: 'Lessons on money and financial independence.',
      authorFirstName: 'Robert',
      authorLastName: 'Kiyosaki',
      publisherName: 'Plata Publishing',
      firstPageUrl: "http://books.google.com/books/content?id=8QFwvgAACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api",
      genres: [{ id: 8, label: 'Finance' }]
    },
    {
      id: 13,
      isbn: '9781455586691',
      title: 'Deep Work',
      year: 2016,
      quantity: 6,
      description: 'Rules for focused success in a distracted world.',
      authorFirstName: 'Cal',
      authorLastName: 'Newport',
      publisherName: 'Grand Central Publishing',
      firstPageUrl: "http://books.google.com/books/content?id=foeNrgEACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api",
      genres: [{ id: 9, label: 'Productivity' }]
    },
    {
      id: 14,
      isbn: '9780374533557',
      title: 'Thinking, Fast and Slow',
      year: 2011,
      quantity: 7,
      description: 'A deep dive into human decision-making.',
      authorFirstName: 'Daniel',
      authorLastName: 'Kahneman',
      publisherName: 'Farrar, Straus and Giroux',
      firstPageUrl: "https://m.media-amazon.com/images/I/61fdrEuPJwL._AC_UF1000,1000_QL80_.jpg",
      genres: [{ id: 10, label: 'Psychology' }]
    },
    {
      id: 15,
      isbn: '9780307887894',
      title: 'The Lean Startup',
      year: 2011,
      quantity: 9,
      description: 'A new approach to building startups.',
      authorFirstName: 'Eric',
      authorLastName: 'Ries',
      publisherName: 'Crown Business',
      firstPageUrl: "http://books.google.com/books/content?id=r9x-OXdzpPcC&printsec=frontcover&img=1&zoom=2&source=gbs_api",
      genres: [{ id: 11, label: 'Business' }]
    }
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