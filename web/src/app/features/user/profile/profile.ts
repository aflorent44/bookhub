import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';

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

  profileForm!: FormGroup;
  passwordForm!: FormGroup;

  ngOnInit(): void {
    this.initForms();
    this.loadData();
  }

  private initForms(): void {
    this.profileForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      phone: ['', [Validators.pattern(/^(\+33|0)[1-9](\d{8})$/)]],
    });

    this.passwordForm = this.fb.group(
      {
        currentPassword: ['', Validators.required],
        newPassword: [
          '',
          [
            Validators.required,
            Validators.minLength(8),
            Validators.pattern(/^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{8,}$/),
          ],
        ],
        confirmPassword: ['', Validators.required],
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
          email: user.email ?? '',
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

  saveProfile(): void {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }
    this.savingProfile.set(true);
    // this.profileService.updateProfile(this.profileForm.value).subscribe({
    //   next: (updated) => {
    //     this.user.set(updated);
    //     this.savingProfile.set(false);
    //     this.showSuccess('Profil mis à jour avec succès !');
    //   },
    //   error: () => {
    //     this.savingProfile.set(false);
    //     this.showError('Erreur lors de la mise à jour du profil.');
    //   },
    // });
  }

  //TODO
  changePassword(): void {
    if (this.passwordForm.invalid) {
      this.passwordForm.markAllAsTouched();
      return;
    }
    // this.savingPassword.set(true);
    // this.profileService.changePassword(this.passwordForm.value).subscribe({
    //   next: () => {
    //     this.savingPassword.set(false);
    //     this.passwordForm.reset();
    //     this.showSuccess('Mot de passe modifié avec succès !');
    //   },
    //   error: (err) => {
    //     this.savingPassword.set(false);
    //     const msg =
    //       err.status === 400
    //         ? 'Mot de passe actuel incorrect.'
    //         : 'Erreur lors du changement de mot de passe.';
    //     this.showError(msg);
    //   },
    // });
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
            // TODO: Rediriger vers login + logout
            // inject(Router).navigate(['/login']);
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
}
