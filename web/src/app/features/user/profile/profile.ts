import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { ProfileForm } from '../../../core/type/profile-form';
import { PasswordForm } from '../../../core/type/password-form';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { TagModule } from 'primeng/tag';
import { DividerModule } from 'primeng/divider';
import { ConfirmationService, MessageService } from 'primeng/api';
import { FloatLabelModule } from 'primeng/floatlabel';
import { UserResponse } from '../../../core/type/user-response';
import { ProfileService } from '../../../core/service/profile-service';
import { Reservation } from "../../../core/type/reservation";
import { ReservationStates } from "../../../core/type/reservation-states";
import { Loan } from "../../../core/type/loan";

function passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
  const newPwd = control.get('newPassword')?.value;
  const confirmPwd = control.get('confirmPassword')?.value;
  return newPwd && confirmPwd && newPwd !== confirmPwd ? { passwordMismatch: true } : null;
}

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CardModule,
    InputTextModule,
    ButtonModule,
    ToastModule,
    ProgressSpinnerModule,
    ConfirmDialogModule,
    TagModule,
    DividerModule,
    FloatLabelModule,
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
})
export class Profile implements OnInit {
  private readonly profileService = inject(ProfileService);
  private readonly fb = inject(FormBuilder);
  private readonly messageService = inject(MessageService);
  private readonly confirmationService = inject(ConfirmationService);

  user = signal<UserResponse | null>(null);
  loading = signal(true);
  savingProfile = signal(false);
  savingPassword = signal(false);
  reservations = signal<Reservation[]>([]);
  loadingReservations = signal(false);
  loans = signal<Loan[]>([]);
  loadingLoans = signal(false);
  currentLoans = computed(() =>
    this.loans().filter(loan => loan.status === 'WAITING' || loan.status === 'IN_PROGRESS')
  );
  loanHistory = computed(() =>
    this.loans().filter(loan => loan.status === 'FINISHED')
  );
  reservationCount = computed(() =>
    this.reservations().filter(reservation => reservation.status === 'WAITING')
  );

  profileForm!: FormGroup<ProfileForm>;
  passwordForm!: FormGroup<PasswordForm>;

  ngOnInit(): void {
    this.initForms();
    this.loadData();
    this.loadReservations();
    this.loadLoans();
  }

  private initForms(): void {
    this.profileForm = this.fb.group({
      firstName: this.fb.nonNullable.control('', [Validators.required, Validators.minLength(2)]),
      lastName: this.fb.nonNullable.control('', [Validators.required, Validators.minLength(2)]),
      phoneNumber: this.fb.nonNullable.control('', [Validators.pattern(/^(\+33|0)[1-9](\d{8})$/)]),
    });

    this.passwordForm = this.fb.group(
      {
        currentPassword: this.fb.nonNullable.control('', Validators.required),
        newPassword: this.fb.nonNullable.control('', [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(/^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{8,}$/),
        ]),
        confirmPassword: this.fb.nonNullable.control('', Validators.required),
      },
      { validators: passwordMatchValidator },
    );
  }

  private loadData(): void {
    this.loading.set(true);
    this.profileService.getProfile().subscribe({
      next: (user) => {
        this.user.set(user);
        this.profileForm.patchValue({
          firstName: user.firstname,
          lastName: user.lastname,
          phoneNumber: user.phoneNumber ?? '',
        });
        this.loading.set(false);
      },
      error: (err) => {
        this.loading.set(false);
        this.showError('Impossible de charger votre profil.');
        console.error('Profile load error:', err);
      },
    });
  }

  private loadLoans(): void {
    this.loadingLoans.set(true);

    this.profileService.getMyLoans().subscribe({
      next: (loans) => {
        this.loans.set(loans);
        this.loadingLoans.set(false);
      },
      error: (err) => {
        this.loadingLoans.set(false);
        this.showError('Impossible de charger vos emprunts.');
        console.error('Loans load error:', err);
      }
    });
  }

  saveProfile(): void {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }

    this.savingProfile.set(true);

    const payload = this.profileForm.getRawValue();

    this.profileService.updateProfile(payload).subscribe({
      next: (updated) => {
        this.user.set(updated);
        this.profileForm.patchValue({
          firstName: updated.firstname,
          lastName: updated.lastname,
          phoneNumber: updated.phoneNumber ?? '',
        });
        this.savingProfile.set(false);
        this.showSuccess('Profil mis à jour avec succès !');
      },
      error: () => {
        this.savingProfile.set(false);
        this.showError('Erreur lors de la mise à jour du profil.');
      },
    });
  }

  changePassword(): void {
    if (this.passwordForm.invalid) {
      this.passwordForm.markAllAsTouched();
      return;
    }

    this.savingPassword.set(true);

    const { currentPassword, newPassword } = this.passwordForm.getRawValue();

    this.profileService.changePassword({ currentPassword, newPassword }).subscribe({
      next: () => {
        this.savingPassword.set(false);
        this.passwordForm.reset();
        this.showSuccess('Mot de passe modifié avec succès !');
      },
      error: (err) => {
        this.savingPassword.set(false);
        const message = err?.error?.message || 'Erreur lors de la modification du mot de passe.';
        this.showError(message);
      },
    });
  }

  confirmDeleteAccount(): void {
    this.confirmationService.confirm({
      header: 'Supprimer mon compte',
      message:
        'Cette action est irréversible. Toutes vos données seront supprimées conformément au RGPD. Confirmer ?',
      acceptLabel: 'Oui, supprimer',
      rejectLabel: 'Annuler',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.profileService.deleteAccount().subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Compte supprimé',
              detail: 'Votre compte a été supprimé avec succès.',
              life: 5000,
            });
          },
          error: () => this.showError('Impossible de supprimer le compte.'),
        });
      },
    });
  }

  getRoleLabel(role: string): string {
    const labels: Record<string, string> = {
      USER: 'Lecteur',
      LIBRARIAN: 'Bibliothécaire',
      ADMIN: 'Administrateur',
    };
    return labels[role] ?? role;
  }

  getRoleSeverity(role: string): 'success' | 'info' | 'warn' | 'danger' | 'secondary' | 'contrast' {
    const map: Record<string, 'success' | 'info' | 'warn'> = {
      USER: 'info',
      LIBRARIAN: 'success',
      ADMIN: 'warn',
    };
    return map[role] ?? 'info';
  }

  getInitials(user: UserResponse): string {
    return `${user.firstname[0]}${user.lastname[0]}`.toUpperCase();
  }

  hasError(form: FormGroup, field: string, error: string): boolean {
    const ctrl = form.get(field);
    return !!(ctrl?.hasError(error) && ctrl.touched);
  }

  private showSuccess(msg: string): void {
    this.messageService.add({ severity: 'success', summary: 'Succès', detail: msg, life: 3000 });
  }

  private showError(msg: string): void {
    this.messageService.add({ severity: 'error', summary: 'Erreur', detail: msg, life: 4000 });
  }

  resetProfileForm(): void {
    const user = this.user();
    this.profileForm.reset({
      firstName: user?.firstname ?? '',
      lastName: user?.lastname ?? '',
      phoneNumber: user?.phoneNumber ?? '',
    });
  }

  private loadReservations(): void {
    this.loadingReservations.set(true);
    this.profileService.getMyReservations().subscribe({
      next: (reservations) => {
        this.reservations.set(reservations);
        this.loadingReservations.set(false);
      },
      error: (err) => {
        this.loadingReservations.set(false);
        this.showError('Impossible de charger vos réservations.');
        console.error('Reservations load error:', err);
      },
    });
  }

  getReservationStatus(status: ReservationStates): string {
    const labels: Record<ReservationStates, string> = {
      [ReservationStates.WAITING]: 'En attente',
      [ReservationStates.AVAILABLE]: 'Disponible',
      [ReservationStates.CANCELED]: 'Annulée',
    };
    return labels[status];
  }

  getReservationSeverity(status: ReservationStates): 'info' | 'success' | 'danger' {
    const map: Record<ReservationStates, 'info' | 'success' | 'danger'> = {
      [ReservationStates.WAITING]: 'info',
      [ReservationStates.AVAILABLE]: 'success',
      [ReservationStates.CANCELED]: 'danger',
    };
    return map[status];
  }

  confirmCancelReservation(reservation: Reservation): void {
    this.confirmationService.confirm({
      header: 'Annuler la réservation',
      message: `Voulez-vous vraiment annuler votre réservation pour "${reservation.book.title}" ?`,
      acceptLabel: 'Oui, annuler',
      rejectLabel: 'Non',
      acceptButtonStyleClass: 'p-button-danger p-button-outlined',
      accept: () => {
        this.cancelReservation(reservation.id);
      },
    });
  }

  private cancelReservation(id: number) {
    this.profileService.cancelReservation(id).subscribe({
      next: () => {
        const updated = this.reservations().filter(reservation => reservation.id !== id);
        this.reservations.set(updated);
        this.showSuccess('Réservation annulée avec succès.');
      },
      error: () => {
        this.showError('Erreur lors de l\'annulation.');
        this.loadReservations();
      }
    })
  }

  getLoanStatus(status: Loan['status']): string {
    const labels: Record<Loan['status'], string> = {
      WAITING: 'En attente',
      IN_PROGRESS: 'En cours',
      FINISHED: 'Terminé',
      LATE: 'En retard'
    };
    return labels[status] || status;
  }

  getLoanSeverity(status: Loan['status']): 'info' | 'success' | 'secondary'  {
    const map: Record<Loan['status'], 'info' | 'success' | 'secondary'> = {
      WAITING: 'info',
      IN_PROGRESS: 'success',
      FINISHED: 'secondary',
      LATE: 'secondary',
    };
    return map[status] || 'info';
  }
  protected readonly ReservationStates = ReservationStates;
}
