import { Component, Input, ChangeDetectionStrategy } from '@angular/core';

export interface Book {
  id: number;
  title: string;
  authorFirstName: string;
  authorLastName: string;
  coverUrl: string;
}

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