import { Component, computed, inject, OnInit, signal } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { CommonModule } from '@angular/common';
import { Book } from '../../../core/type/book';
import { BookService } from '../../../core/service/book.service';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { TagModule } from 'primeng/tag';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { Button } from '../../../shared/components/button/button';
import { LoanService } from '../../../core/service/loan.service';
import { AuthService } from '../../../core/service/auth-service';
import {LoanStatusPipe} from '../../../core/pipes/loan-status-pipe';
import {ConfirmationService, MessageService} from 'primeng/api';
import {ToastModule} from 'primeng/toast';
import {ConfirmDialog} from 'primeng/confirmdialog';
import {ReservationService} from '../../../core/service/reservation.service';
import {Loan} from '../../../core/type/loan';
import {ReviewService} from '../../../core/service/review.service';
import {ReviewSection} from '../../review-section/review-section';
import {Rating} from 'primeng/rating';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-book-detail',
  standalone: true,
  imports: [CommonModule, CardModule, DividerModule, TagModule, ProgressSpinnerModule, Button, LoanStatusPipe, ToastModule, ConfirmDialog, ReviewSection, Rating, FormsModule],
  providers: [ConfirmationService, MessageService],
  templateUrl: './book-detail.html',
})
export class BookDetail implements OnInit {

  private route = inject(ActivatedRoute);
  router = inject(Router);
  private bookService = inject(BookService);
  private loanService = inject(LoanService);
  private authService = inject(AuthService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  private reservationService = inject(ReservationService);
  private reviewService = inject(ReviewService);

  book = signal<Book | null>(null);
  error = signal('');
  loans = signal<any[]>([]);
  reservations = signal<any[]>([]);
  reviews = signal<any[]>([]);
  userLoans = signal<Loan[]>([]);
  userReservations = signal<any[]>([]);
  completedLoansPage = signal(0);
  averageBookRating = signal(0);
  bookReviewsCount = signal(0);
  completedLoansPageSize = 5;

  isAdminOrLibrarian = computed(() => {
    const role = this.authService.currentUser$()?.role;
    return role === 'ADMIN' || role === 'LIBRARIAN';
  });

  isUser = computed(() => {
    const role = this.authService.currentUser$()?.role;
    return role === 'USER';
  });

  completedLoans = computed(() =>
    this.loans()
      .filter(l => l.status === 'FINISHED')
      .sort((a, b) => new Date(b.returnDate).getTime() - new Date(a.returnDate).getTime())
  );

  completedLoansPaged = computed(() => {
    const start = this.completedLoansPage() * this.completedLoansPageSize;
    return this.completedLoans().slice(start, start + this.completedLoansPageSize);
  });

  completedLoansTotalPages = computed(() =>
    Math.ceil(this.completedLoans().length / this.completedLoansPageSize)
  );

  private averageRating = computed(() => {
    if (!this.reviews().length) return 0;
    const avg = this.reviews().reduce((sum, r) => sum + r.rating, 0) / this.reviews().length;
    return Math.round(avg * 100) / 100;
  });

  get avgRating() {
    return this.averageRating();
  }

  prevCompletedPage() {
    if (this.completedLoansPage() > 0) {
      this.completedLoansPage.update(p => p - 1);
    }
  }

  nextCompletedPage() {
    if (this.completedLoansPage() < this.completedLoansTotalPages() - 1) {
      this.completedLoansPage.update(p => p + 1);
    }
  }

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id') ?? '');

    this.bookService.getBookById(id).subscribe({
      next: (book) => {
        this.book.set(book);
        this.loadReviews(id);

        if (this.isAdminOrLibrarian()) {
          this.loadLoans(id);
          this.loadReservations(id);
        }

        if (this.isUser()) {
          const userId = this.authService.currentUser$()!.id;
          this.loadUserLoansAndReservations(userId, id);
        }
      },
      error: (err) => console.error('Erreur :', err)
    });
  }

  reloadBook() {
    const id = Number(this.route.snapshot.paramMap.get('id') ?? '');
    this.bookService.getBookById(id).subscribe({
      next: (book) => this.book.set(book),
      error: (err) => console.error('Erreur reload book :', err)
    });
  }

  private reloadAll() {
    const id = Number(this.route.snapshot.paramMap.get('id') ?? '');
    this.reloadBook();
    this.loadReviews(id);
    if (this.isAdminOrLibrarian()) {
      this.loadLoans(id);
      this.loadReservations(id);
    }
    if (this.isUser()) {
      this.loadUserLoansAndReservations();
    }
  }

  loadLoans(bookId: number) {
    this.loanService.getLoansByBookId(bookId).subscribe({
      next: (loans) => this.loans.set(loans),
      error: (err) => console.error('Erreur loans :', err)
    });
  }

  loadReservations(bookId: number) {
    this.reservationService.getReservationsByBookId(bookId).subscribe({
      next: (reservations) => this.reservations.set(reservations),
      error: (err) => console.error('Erreur réservations :', err)
    });
  }

  loadUserLoansAndReservations(userId?: number, bookId?: number) {
    const uid = userId ?? this.authService.currentUser$()!.id;
    const bid = bookId ?? Number(this.route.snapshot.paramMap.get('id') ?? '');

    this.loanService.getLoansByUserAndBook(uid, bid).subscribe({
      next: (loans) => {
        this.userLoans.set(
          [...loans].sort((a, b) =>
            new Date(b.debutDate).getTime() - new Date(a.debutDate).getTime()
          )
        );
      },
      error: (err) => console.error('Erreur user loans :', err)
    });

    this.reservationService.getReservationsByUserAndBook(uid, bid).subscribe({
      next: (reservations) => this.userReservations.set(reservations),
      error: (err) => console.error('Erreur user reservations :', err)
    });
  }

  loadReviews(bookId: number) {
    this.reviewService.getReviewsByBookId(bookId).subscribe({
      next: (reviews) => {
        this.reviews.set(reviews);
        const avg = reviews.length
          ? Math.round(reviews.reduce((s, r) => s + r.rating, 0) / reviews.length)
          : 0;
        this.averageBookRating.set(avg);
        this.bookReviewsCount.set(reviews.length);
      },
      error: (err) => console.error('Erreur reviews :', err)
    });
  }

  getDaysLate(endDate: string): number {
    const end = new Date(endDate);
    const now = new Date();
    const diff = Math.floor((now.getTime() - end.getTime()) / (1000 * 60 * 60 * 24));
    return diff > 0 ? diff : 0;
  }

  onEditBook() {
    const id = this.book()?.bookId;
    if (id) {
      this.router.navigate(['/books', id, 'edit']);
    }
  }

  onValidateLoan(loanId: number) {
    this.loanService.validateLoan(loanId).subscribe({
      next: () => this.reloadAll(),
      error: (err) => this.error.set(this.loanService.getErrorMessage(err.message))
    });
  }

  onReturnBook(loan: Loan) {
    this.loanService.returnBook(loan.id, loan.userId, loan.bookId).subscribe({
      next: () => {
        this.reloadAll();
        this.messageService.add({
          severity: 'success',
          summary: 'Retour enregistré',
          detail: 'Le livre a bien été retourné.',
          life: 4000
        });
      },
      error: (err) => this.error.set(this.loanService.getErrorMessage(err.message))
    });
  }

  onLoan(bookId: number) {
    this.confirmationService.confirm({
      message: 'Voulez-vous vraiment emprunter ce livre ? Vous devrez venir le récupérer à la librairie.',
      header: 'Confirmer l\'emprunt',
      icon: 'pi pi-book',
      acceptLabel: 'Oui, emprunter',
      rejectLabel: 'Annuler',
      accept: () => {
        this.loanService.loanBook(bookId).subscribe({
          next: () => {
            this.error.set('');
            this.reloadAll();
            this.messageService.add({
              severity: 'success',
              summary: 'Emprunt enregistré',
              detail: 'Votre demande est en attente — veuillez venir à la librairie récupérer votre livre.',
              life: 6000
            });
          },
          error: (err) => {
            this.error.set(this.loanService.getErrorMessage(err.message));
          }
        });
      }
    });
  }

  onReserve(bookId: number) {
    this.confirmationService.confirm({
      message: 'Voulez-vous être mis en liste d\'attente pour ce livre ?',
      header: 'Confirmer la réservation',
      icon: 'pi pi-clock',
      acceptLabel: 'Oui, réserver',
      rejectLabel: 'Annuler',
      accept: () => {
        this.reservationService.reserveBook(bookId).subscribe({
          next: () => {
            this.error.set('');
            this.reloadAll();
            this.messageService.add({
              severity: 'info',
              summary: 'Réservation enregistrée',
              detail: 'Vous serez notifié lorsque le livre sera disponible.',
              life: 6000
            });
          },
          error: (err) => {
            this.error.set(this.reservationService.getErrorMessage(err.message));
          }
        });
      }
    });
  }

}


