import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { ButtonModule } from 'primeng/button';
import { DrawerModule } from 'primeng/drawer';
import { BookFilters } from '../../../../core/type/book-filters';

interface FilterFields {
  authorFirstName: string;
  authorLastName: string;
  genre: string;
  publisher: string;
  year: number | null;
  countryName: string;
  countryNationality: string;
}

@Component({
  selector: 'app-catalog-filters',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, InputTextModule, FloatLabelModule, ButtonModule, DrawerModule],
  templateUrl: './catalog-filters.html',
  styleUrl: './catalog-filters.scss',
})
export class CatalogFilters implements OnInit {
  private readonly fb = inject(FormBuilder);

  filters = input.required<BookFilters>();
  visible = input<boolean>(false);
  filtersChange = output<Partial<BookFilters>>();
  visibleChange = output<boolean>();

  form!: FormGroup;
  isPristine = signal(true);

  ngOnInit(): void {
    const f = this.filters();
    this.form = this.fb.group({
      authorFirstName: [f.authorFirstName ?? ''],
      authorLastName: [f.authorLastName ?? ''],
      genre: [f.genre ?? ''],
      publisher: [f.publisher ?? ''],
      year: [f.year ?? null],
      countryName: [f.countryName ?? ''],
      countryNationality: [f.countryNationality ?? ''],
    });

    // Suivi des changements pour le bouton reset
    this.form.valueChanges.subscribe(() => {
      this.isPristine.set(this.form.pristine);
    });
  }

  apply(): void {
    if (this.form.valid) {
      const value = this.form.getRawValue() as FilterFields;
      this.filtersChange.emit({
        authorFirstName: value.authorFirstName || '',
        authorLastName: value.authorLastName || '',
        genre: value.genre || '',
        publisher: value.publisher || '',
        year: value.year || undefined,
        countryName: value.countryName || '',
        countryNationality: value.countryNationality || '',
      });
      this.visibleChange.emit(false);
    }
  }

  reset(): void {
    this.form.reset({
      authorFirstName: '',
      authorLastName: '',
      genre: '',
      publisher: '',
      year: null,
      countryName: '',
      countryNationality: '',
    });
    this.filtersChange.emit({});
    this.visibleChange.emit(false);
  }

  close(): void {
    this.visibleChange.emit(false);
  }
}
