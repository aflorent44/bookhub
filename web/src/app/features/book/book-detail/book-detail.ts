import { Component, OnInit } from '@angular/core';
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
  book: Book | null = null;

  constructor(
    private route: ActivatedRoute,
    private bookService: BookService
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id') ?? '';
    this.bookService.getBookById(id).subscribe(data => {
      this.book = data;
    });
  }
}
