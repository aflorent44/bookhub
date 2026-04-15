import { Component, Input, ChangeDetectionStrategy } from '@angular/core';
import { Book } from '../../../core/type/book';

@Component({
  selector: 'app-book-tile',
  imports: [],
  standalone: true,
  templateUrl: './book-tile.html',
  styleUrls: ['./book-tile.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class BookTile {
  @Input() book!: Book;
}