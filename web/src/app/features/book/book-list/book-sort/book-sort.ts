import { Component, EventEmitter, Input, Output } from '@angular/core';

export type SortField = 'title' | 'author' | 'year';
export type SortDirection = 'asc' | 'desc' | null;

@Component({
  selector: 'app-book-sort',
  standalone: true,
  templateUrl: './book-sort.html',
  styleUrls: ['./book-sort.scss']
})
export class BookSort {
  @Input() sortBy: SortField | null = null;
  @Input() sortDirection: SortDirection = null;

  @Output() sortChange = new EventEmitter<{
    sortBy: SortField | null;
    sortDirection: SortDirection;
  }>();

  fields: SortField[] = ['title', 'author', 'year'];

  labels: Record<SortField, string> = {
    title: 'Nom',
    author: 'Auteur',
    year: 'Année'
  };

  // tracks if a field has been clicked before
  private touched = new Set<SortField>();

setSort(field: SortField) {
  let direction: SortDirection;

  if (this.sortBy !== field) {
    direction = 'asc';
  } else if (this.sortDirection === null) {
    direction = 'asc';
  } else if (this.sortDirection === 'asc') {
    direction = 'desc';
  } else {
    direction = null;
  }

  this.sortBy = direction ? field : null;
  this.sortDirection = direction;

  this.sortChange.emit({
    sortBy: this.sortBy,
    sortDirection: this.sortDirection
  });
}

  getIcon(field: SortField): 'up' | 'down' | 'none' {
    if (this.sortBy !== field || this.sortDirection === null) return 'none';
    if (this.sortDirection === 'asc') return 'up';
    if (this.sortDirection === 'desc') return 'down';

    return 'none';
  }
}
