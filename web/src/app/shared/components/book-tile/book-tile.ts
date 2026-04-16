import {Component, Input, ChangeDetectionStrategy, inject} from '@angular/core';
import { Book } from '../../../core/type/book';
import {Button} from '../button/button';
import {Router} from '@angular/router';

@Component({
  selector: 'app-book-tile',
  imports: [
    Button
  ],
  standalone: true,
  templateUrl: './book-tile.html',
  styleUrls: ['./book-tile.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class BookTile {
  private router = inject(Router);
  @Input() book!: Book;

  goToBook(book: Book): void {
    console.log('Go to book', this.book);
    this.router.navigate(['/books', this.book.bookId]);
  }
}
