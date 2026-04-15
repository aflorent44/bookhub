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

@Component({
  selector: 'app-book-detail',
  standalone: true,
  imports: [CommonModule, CardModule, DividerModule, TagModule, ProgressSpinnerModule, Button, LoanStatusPipe, ToastModule, ConfirmDialog],
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

  book = signal<Book | null>(null);
  error = signal('');
  loans = signal<any[]>([]);
  reservations = signal<any[]>([]);
  userLoans = signal<Loan[]>([]);
  userReservations = signal<any[]>([]);

  isAdminOrLibrarian = computed(() => {
    const role = this.authService.currentUser$()?.role;
    return role === 'ADMIN' || role === 'LIBRARIAN';
  });

  isUser = computed(() => {
    const role = this.authService.currentUser$()?.role;
    return role === 'USER';
  });

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id') ?? '');

    this.bookService.getBookById(id).subscribe({
      next: (book) => {
        this.book.set(book);

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
      next: (loans) => this.userLoans.set(loans),
      error: (err) => console.error('Erreur user loans :', err)
    });

    this.reservationService.getReservationsByUserAndBook(uid, bid).subscribe({
      next: (reservations) => this.userReservations.set(reservations),
      error: (err) => console.error('Erreur user reservations :', err)
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
      next: () => {
        // Recharge les loans après validation
        const id = Number(this.route.snapshot.paramMap.get('id') ?? '');
        this.loadLoans(id);
        this.loadReservations(id);
      },
      error: (err) => {
        this.error.set(this.loanService.getErrorMessage(err.message));
      }
    });
  }

  onReturnBook(loan: Loan) {
    this.loanService.returnBook(loan.id, loan.userId, loan.bookId).subscribe({
      next: () => {
        const id = Number(this.route.snapshot.paramMap.get('id') ?? '');
        this.loadLoans(id);
        this.loadReservations(id);
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
            this.messageService.add({
              severity: 'success',
              summary: 'Emprunt enregistré',
              detail: 'Votre demande est en attente — veuillez venir à la librairie récupérer votre livre.',
              life: 6000
            });
            this.loadUserLoansAndReservations();
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
            this.messageService.add({
              severity: 'info',
              summary: 'Réservation enregistrée',
              detail: 'Vous serez notifié lorsque le livre sera disponible.',
              life: 6000
            });
            this.loadUserLoansAndReservations();
          },
          error: (err) => {
            this.error.set(this.reservationService.getErrorMessage(err.message));
          }
        });
      }
    });
  }

}


