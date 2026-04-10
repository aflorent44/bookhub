import { Component, inject, signal, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { FloatLabelModule } from 'primeng/floatlabel';
import { MultiSelectModule } from 'primeng/multiselect';
import { HttpClient } from '@angular/common/http';
import { Book } from '../../../core/types/book';
import { Author } from '../../../core/types/author';
import { Genre } from '../../../core/types/genre';
import {Textarea} from 'primeng/textarea';

@Component({
  selector: 'app-book-form',
  standalone: true,
  imports: [ReactiveFormsModule, InputTextModule, ButtonModule, FloatLabelModule, MultiSelectModule, Textarea],
  templateUrl: './book-form.html',
  styleUrl: './book-form.scss',
})
export class BookForm implements OnInit {
  private http = inject(HttpClient);
  private fb = inject(FormBuilder);

  loading = signal(false);
  error = signal('');

  availableGenres = signal<Genre[]>([
    { id: 1, label: 'Roman' },
    { id: 2, label: 'Science-fiction' },
    { id: 3, label: 'Policier' },
    { id: 4, label: 'Fantastique' },
    { id: 5, label: 'Biographie' },
    { id: 6, label: 'Histoire' },
    { id: 7, label: 'Jeunesse' },
    { id: 8, label: 'Manga' },
    { id: 9, label: 'BD' },
    { id: 10, label: 'Poésie' },
    { id: 11, label: 'Fiction' },
  ]);

  form!: FormGroup;

  ngOnInit() {
    this.form = this.fb.group({
      isbn:            ['', [Validators.required, Validators.minLength(13), Validators.maxLength(13)]],
      title:           ['', Validators.required],
      authorFirstName: ['', Validators.required],
      authorLastName:  ['', Validators.required],
      publisher:       [''],
      year:            [null],
      genres:          [[]],
      synopsis:        [''],
      language:        [''],
      quantity:        [1, [Validators.required, Validators.min(1)]],
    });

    // Écoute les changements de l'ISBN
    this.form.get('isbn')?.valueChanges.subscribe((value: string) => {
      if (value === '') {
        this.resetForm();
      } else if (value?.length === 13) {
        this.fetchBookInfo(value);
      }
    });
  }

  fetchBookInfo(isbn: string) {
    this.loading.set(true);
    this.error.set('');

    const url = `https://www.googleapis.com/books/v1/volumes?q=isbn:${isbn}`;

    this.http.get<any>(url).subscribe({
      next: (data) => {
        if (data.totalItems === 0) {
          this.error.set('Aucun livre trouvé pour cet ISBN.');
          this.loading.set(false);
          return;
        }

        const info = data.items[0].volumeInfo;

        const fullName: string = info.authors?.[0] ?? '';
        const parts = fullName.trim().split(' ');

        const categoryName: string = info.categories?.[0] ?? '';
        const matched = this.availableGenres().find(
          g => g.label.toLowerCase() === categoryName.toLowerCase()
        );

        // patchValue met à jour uniquement les champs spécifiés
        this.form.patchValue({
          title:           info.title ?? '',
          authorFirstName: parts.slice(0, -1).join(' '),
          authorLastName:  parts.at(-1) ?? '',
          publisher:       info.publisher ?? '',
          year:            info.publishedDate ? new Date(info.publishedDate).getFullYear() : null,
          genres:          matched ? [matched] : [],
          synopsis:        info.description ?? '',
          language:        info.language ?? '',
        });

        this.loading.set(false);
      },
      error: () => {
        this.error.set('Erreur lors de la recherche.');
        this.loading.set(false);
      }
    });
  }

  resetForm() {
    this.form.reset({
      isbn:            '',
      title:           '',
      authorFirstName: '',
      authorLastName:  '',
      publisher:       '',
      year:            null,
      genres:          [],
      synopsis:        '',
      language:        '',
      quantity:        1,
    });
    this.error.set('');
  }

  onSubmit() {
    if (this.form.invalid) {
      return;
    }

    const author: Partial<Author> = {
      firstName: this.form.value.authorFirstName,
      lastName:  this.form.value.authorLastName,
    };

    const book: Partial<Book> = {
      isbn:      this.form.value.isbn,
      title:     this.form.value.title,
      author:    author as Author,
      publisher: this.form.value.publisher,
      year:      this.form.value.year,
      genres:    this.form.value.genres,
      synopsis:  this.form.value.synopsis,
      language:  this.form.value.language,
      quantity:  this.form.value.quantity,
    };

    console.log('Livre à enregistrer :', book);
    // TODO : appeler ton service Spring Boot ici
  }
}
