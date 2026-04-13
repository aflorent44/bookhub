import {Component, inject, OnInit, signal} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Book } from '../../../core/type/book';
import { BookService } from '../../../core/service/book.service';
import { CardModule } from 'primeng/card';
import {DividerModule} from 'primeng/divider';
import {TagModule} from 'primeng/tag';
import {ProgressSpinnerModule} from 'primeng/progressspinner';

@Component({
  selector: 'app-book-detail',
  standalone: true,
  imports: [CommonModule, CardModule, DividerModule, TagModule, ProgressSpinnerModule],
  templateUrl: './book-detail.html',
})
export class BookDetail implements OnInit {
  book = signal<Book | null>(null);

  private route = inject(ActivatedRoute);
  private bookService = inject(BookService);

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id') ?? '');
    this.bookService.getBookById(id).subscribe({
      next: (book) => {
        console.log('Book récupéré :', book);
        this.book.set(book);
      },
      error: (err) => {
        console.error('Erreur :', err);
      }
    });
  }
}
