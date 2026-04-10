import { Component, inject, signal, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { FloatLabelModule } from 'primeng/floatlabel';
import {SelectModule} from 'primeng/select';
import { HttpClient } from '@angular/common/http';
import { Author } from '@core/types/author';
import { Country } from '@core/types/country';

@Component({
  selector: 'app-author-form',
  standalone: true,
  imports: [ReactiveFormsModule, InputTextModule, ButtonModule, FloatLabelModule, SelectModule],
  templateUrl: './author-form.html',
  styleUrl: './author-form.scss',
})
export class AuthorForm implements OnInit {
  private http = inject(HttpClient);
  private fb = inject(FormBuilder);

  loading = signal(false);
  error = signal('');

  availableCountries = signal<Country[]>([
    { id: 1, code: 'FRA', name: 'France', nationality: 'Française' },
    { id: 2, code: 'GBR', name: 'Royaume-Uni', nationality: 'Britannique' },
    { id: 3, code: 'USA', name: 'États-Unis', nationality: 'Américaine' },
    { id: 4, code: 'DEU', name: 'Allemagne', nationality: 'Allemande' },
    { id: 5, code: 'ESP', name: 'Espagne', nationality: 'Espagnole' },
    { id: 6, code: 'ITA', name: 'Italie', nationality: 'Italienne' },
    { id: 7, code: 'JPN', name: 'Japon', nationality: 'Japonaise' },
    { id: 8, code: 'RUS', name: 'Russie', nationality: 'Russe' },
    { id: 9, code: 'BRA', name: 'Brésil', nationality: 'Brésilienne' },
    { id: 10, code: 'ARG', name: 'Argentine', nationality: 'Argentine' },
  ]);

  form!: FormGroup;

  ngOnInit() {
    this.form = this.fb.group({
      firstName: ['', Validators.required],
      lastName:  ['', Validators.required],
      countryId: [null, Validators.required],
    });
  }

  fetchCountries() {
    this.loading.set(true);
    this.http.get<Country[]>('http://localhost:3000/api/countries').subscribe({
      next: (data) => {
        this.availableCountries.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Erreur lors du chargement des pays.');
        this.loading.set(false);
      }
    });
  }

  resetForm() {
    this.form.reset({
      firstName: '',
      lastName:  '',
      countryId: null,
    });
    this.error.set('');
  }

  onSubmit() {
    if (this.form.invalid) return;

    const author: Partial<Author> = {
      firstName: this.form.value.firstName,
      lastName:  this.form.value.lastName,
      country:   { id: this.form.value.countryId } as Country,
    };

    console.log('Auteur à enregistrer :', author);
    // TODO : appeler ton service Spring Boot ici
  }
}
