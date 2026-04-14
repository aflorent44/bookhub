import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
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
import {MessageService} from 'primeng/api';
import {ToastModule} from 'primeng/toast';

@Component({
  selector: 'app-book-detail',
  standalone: true,
  imports: [CommonModule, CardModule, DividerModule, TagModule, ProgressSpinnerModule, Button, LoanStatusPipe, ToastModule],
  providers: [MessageService],
  templateUrl: './book-detail.html',
})
export class BookDetail implements OnInit {

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private bookService = inject(BookService);
  private loanService = inject(LoanService);
  private authService = inject(AuthService);
  private messageService = inject(MessageService);

  book = signal<Book | null>(null);
  error = signal('');
  loans = signal<any[]>([]);

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
        }
      },
      error: (err) => console.error('Erreur :', err)
    });
  }

  onValidateLoan(loanId: number) {
    this.loanService.validateLoan(loanId).subscribe({
      next: () => {
        // Recharge les loans après validation
        const id = Number(this.route.snapshot.paramMap.get('id') ?? '');
        this.loadLoans(id);
      },
      error: (err) => {
        this.error.set(this.loanService.getErrorMessage(err.message));
      }
    });
  }

  loadLoans(bookId: number) {
    this.loanService.getLoansByBookId(bookId).subscribe({
      next: (loans) => this.loans.set(loans),
      error: (err) => console.error('Erreur loans :', err)
    });
  }

  onLoan(bookId: number) {
    this.loanService.loanBook(bookId).subscribe({
      next: () => {
        this.error.set('');
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
}
