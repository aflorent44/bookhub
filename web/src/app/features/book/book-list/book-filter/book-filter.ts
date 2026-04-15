import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BookFilters } from '../book-list/book-list';
import { Button } from '../../../../shared/components/button/button'

@Component({
  selector: 'app-book-filter',
  standalone: true,
  imports: [CommonModule, FormsModule, Button ],
  templateUrl: './book-filter.html',
  styleUrl: './book-filter.scss',
})
export class BookFilter {
  @Input() filters!: BookFilters;
  @Output() filtersChange = new EventEmitter<Partial<BookFilters>>();

  localFilters: Partial<BookFilters> = {};

  ngOnInit(): void {
    this.localFilters = { ...this.filters };
  }

  applyFilters(): void {
    this.filtersChange.emit(this.localFilters);
  }

  resetFilters(): void {
    this.localFilters = {};
    this.filtersChange.emit({});
  }
}