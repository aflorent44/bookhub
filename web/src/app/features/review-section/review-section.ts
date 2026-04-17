import {Component, inject, Input, OnInit, signal, computed} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormsModule} from '@angular/forms';
import {ReviewService} from '../../core/service/review.service';
import {AuthService} from '../../core/service/auth-service';
import {Review} from '../../core/type/review';
import {CardModule} from 'primeng/card';
import {DividerModule} from 'primeng/divider';
import {RatingModule} from 'primeng/rating';
import {TextareaModule} from 'primeng/textarea';
import {ButtonModule} from 'primeng/button';
import {Book} from '../../core/type/book';
import {ConfirmationService, MessageService} from 'primeng/api';
import {ToastModule} from 'primeng/toast';
import {ConfirmDialog} from 'primeng/confirmdialog';

@Component({
  selector: 'app-review-section',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, CardModule, DividerModule, RatingModule, TextareaModule, ButtonModule, FormsModule, ToastModule, ConfirmDialog],
  providers: [ConfirmationService, MessageService],
  templateUrl: './review-section.html',
  styleUrl: './review-section.scss',
})
export class ReviewSection implements OnInit {

  private _book!: Book;
  private formBuilder = inject(FormBuilder);
  private reviewService = inject(ReviewService);
  private authService = inject(AuthService);
  private confirmationService = inject(ConfirmationService);
  private messageService = inject(MessageService);

  reviews = signal<Review[]>([]);
  error = signal('');
  isEditMode = signal(false);
  editReviewId = signal<number | null>(null);
  loading = signal(false);
  currentUser = this.authService.currentUser$;

  @Input() set book(value: Book) {
    if (value) {
      this._book = value;
      if (this.form) { // évite l'appel avant ngOnInit
        this.loadReviews();
      }
    }
  }

  get book(): Book {
    return this._book;
  }

  form!: FormGroup;

  isUserAndIsLoggedIn(): boolean {
    const user = this.authService.currentUser$();
    return !!user && user.role === 'USER';
  }

  hasAlreadyReviewed = computed(() => {
    const userId = this.currentUser()?.id;
    if (!userId) return false;
    return this.reviews().some(r => r.user?.id === userId);
  });

  ngOnInit() {
    this.form = this.formBuilder.group({
      rating:  [0, [Validators.required, Validators.min(1), Validators.max(5)]],
      comment: ['', [Validators.required, Validators.minLength(3)]],
    });

    if (this._book) {
      this.loadReviews();
    }
  }

  loadReviews() {
    this.reviewService.getReviewsByBookId(this.book.bookId).subscribe({
      next: (reviews) => {
        this.reviews.set(
          [...reviews].sort((a, b) =>
            new Date(b.createdAt ?? '').getTime() - new Date(a.createdAt ?? '').getTime()
          )
        );
      },
      error: (err) => {
        this.error.set(this.getErrorMessage(err.message));
        this.loading.set(false);
      }
    });
  }

  isCurrentUserReview(review: Review): boolean {
    return review.user?.id === this.currentUser()!.id;
  }

  onEditReview(review: Review) {
    this.isEditMode.set(true);
    this.editReviewId.set(review.reviewId ?? null);
    this.form.patchValue({
      rating: review.rating,
      comment: review.comment,
    });
  }

  onCancelEdit() {
    this.isEditMode.set(false);
    this.editReviewId.set(null);
    this.form.reset({rating: 0, comment: ''});
  }

  resetForm() {
    this.form.reset({
      rating: 0,
      comment: ''
    });
    this.error.set('');
  }

  onDeleteReview(reviewId: number) {
    this.confirmationService.confirm({
      message: 'Voulez-vous vraiment supprimer cet avis ?',
      header: 'Confirmer la suppression',
      icon: 'pi pi-trash',
      acceptLabel: 'Oui, supprimer',
      rejectLabel: 'Annuler',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.reviewService.deleteReview(reviewId).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Avis supprimé',
              detail: 'Votre avis a bien été supprimé.',
              life: 3000
            });
            this.loadReviews();
          },
          error: () => this.error.set('Erreur lors de la suppression.')
        });
      }
    });
  }

  onSubmit() {
    if (this.form.invalid) return;

    const review = {
      reviewId:       this.isEditMode() ? this.editReviewId() ?? undefined : undefined,
      bookId:         this.book.bookId,
      userId:         this.currentUser()!.id,
      internalUserId: this.currentUser()!.id,
      rating:         this.form.value.rating,
      comment:        this.form.value.comment,
      isHidden:       false,
    };

    const request$ = this.isEditMode()
      ? this.reviewService.updateReview(review)
      : this.reviewService.addReview(review);

    request$.subscribe({
      next: () => {
        this.isEditMode.set(false);
        this.editReviewId.set(null);
        this.form.reset({ rating: 0, comment: '' });
        this.loadReviews();
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(this.getErrorMessage(err.message));
        this.loading.set(false);
      }
    });

  }

  public getErrorMessage(code: string): string {
    const messages: Record<string, string> = {
      '10001': 'Livre introuvable.',
      '10002': 'La note doit être comprise entre 1 et 5.',
      '10003': 'Avis introuvable.',
      '10004': 'Vous devez avoir emprunté ce livre pour laisser un avis.',
      '10006': 'Vous avez déjà laissé un avis sur ce livre.',
      '10011': 'Avis introuvable.',
    };
    return messages[code] ?? 'Erreur lors de l\'envoi de l\'avis.';
  }
}
