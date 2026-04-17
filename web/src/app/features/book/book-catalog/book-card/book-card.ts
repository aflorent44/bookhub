import { Component, ChangeDetectionStrategy, inject, input } from '@angular/core';
import { Router } from "@angular/router";
import { Book } from '../../../../core/type/book';

@Component({
  selector: 'app-book-card',
  standalone: true,
  templateUrl: './book-card.html',
  styleUrl: './book-card.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class BookCard {
  private readonly router = inject(Router);
  book = input.required<Book>();

  goToDetail(): void {
    this.router.navigate(['/books', this.book().bookId]);
  }
}
