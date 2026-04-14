import { Component, inject, signal, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { FloatLabelModule } from 'primeng/floatlabel';
import { MultiSelectModule } from 'primeng/multiselect';
import { Genre } from '../../../core/type/genre';
import { Textarea } from 'primeng/textarea';
import { BookService } from '../../../core/service/book.service';
import { GenreService } from '../../../core/service/genre.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-book-form',
  standalone: true,
  imports: [ReactiveFormsModule, InputTextModule, ButtonModule, FloatLabelModule, MultiSelectModule, Textarea],
  templateUrl: './book-form.html',
  styleUrl: './book-form.scss',
})
export class BookForm implements OnInit {

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private bookService = inject(BookService);
  private genreService = inject(GenreService);

  loading = signal(false);
  error = signal('');

  availableGenres = signal<Genre[]>([]);

  private getErrorMessage(err: any): string {
    const code: string = err?.message ?? '';
    const messages: Record<string, string> = {
      '1031': 'Ce livre existe déjà (ISBN déjà enregistré).',
      '1021': 'Le titre est requis.',
      '1022': 'L\'ISBN est requis.',
      '1023': 'Au moins un genre est requis.',
    };
    return messages[code] ?? 'Erreur lors de la création du livre.';
  }

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
      description:     [''],
      language:        [''],
      quantity:        [1, [Validators.required, Validators.min(1)]],
      firstPageUrl:    [''],
    });

    this.genreService.getGenres().subscribe({
      next: (genres) => {
        this.availableGenres.set(genres);

        // Écoute les changements de l'ISBN seulement après chargement des genres
        this.form.get('isbn')?.valueChanges.subscribe((value: string) => {
          if (value === '') {
            this.resetForm();
          } else if (value?.length === 13) {
            this.fetchBookInfo(value);
          }
        });
      },
      error: () => this.error.set('Erreur lors du chargement des genres.')
    });
  }

  private patchBookInfo(info: any, parts: string[]) {
    this.form.patchValue({
      title:           info.title ?? '',
      authorFirstName: parts.slice(0, -1).join(' '),
      authorLastName:  parts.at(-1) ?? '',
      publisher:       info.publisher ?? '',
      year:            info.publishedDate ? new Date(info.publishedDate).getFullYear() : null,
      description:     info.description ?? '',
      language:        info.language ?? '',
      firstPageUrl:    info.imageLinks?.thumbnail ?? null,
    });
  }

  fetchBookInfo(isbn: string) {
    this.loading.set(true);
    this.error.set('');

    this.bookService.getBookInfoFromGoogleByIsbn(isbn).subscribe({
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

        if (categoryName) {
          const matched = this.availableGenres().find(
            g => g.label.toLowerCase() === categoryName.toLowerCase()
          );

          if (matched) {
            this.form.patchValue({ genres: [matched] });
            this.patchBookInfo(info, parts);
            this.loading.set(false);
          } else {
            this.genreService.createGenre({ id: 0, label: categoryName }).subscribe({
              next: (created) => {
                this.availableGenres.update(genres => [...genres, created]);
                this.form.patchValue({ genres: [created] });
                this.patchBookInfo(info, parts);
                this.loading.set(false);
              },
              error: () => {
                this.error.set('Erreur lors de la création du genre.');
                this.loading.set(false);
              }
            });
          }
        } else {
          this.patchBookInfo(info, parts);
          this.loading.set(false);
        }
      },
      error: (err) => {
        this.error.set(this.getErrorMessage(err));
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
      description:     '',
      language:        '',
      firstPageUrl:    '',
      quantity:        1,
    });
    this.error.set('');
  }

  onSubmit() {
    if (this.form.invalid) return;

    const book = {
      isbn:            this.form.value.isbn,
      title:           this.form.value.title,
      year:            this.form.value.year,
      quantity:        this.form.value.quantity,
      description:     this.form.value.description,
      firstPageUrl:    this.form.value.firstPageUrl ?? null,
      authorFirstName: this.form.value.authorFirstName,
      authorLastName:  this.form.value.authorLastName,
      publisherName:   this.form.value.publisher,
      countryName:     null,
      genres:          this.form.value.genres,
      createdById:     null,
    };

    this.loading.set(true);
    this.bookService.createBook(book).subscribe({
      next: (created) => {
        console.log('Livre créé :', created);
        this.loading.set(false);
        this.resetForm();
        this.router.navigate(['/books', created.id]);
      },
      error: (err) => {
        this.error.set(this.getErrorMessage(err));
        this.loading.set(false);
      }
    });
  }
}
