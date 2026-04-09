import {CommonModule} from '@angular/common';
import {Component, inject} from '@angular/core';
import {AbstractControl, FormBuilder, ReactiveFormsModule, ValidationErrors, Validators} from '@angular/forms';

import {ButtonModule} from 'primeng/button';
import {CardModule} from 'primeng/card';
import {FloatLabelModule} from 'primeng/floatlabel';
import {InputGroupModule} from 'primeng/inputgroup';
import {InputGroupAddonModule} from 'primeng/inputgroupaddon';
import {InputTextModule} from 'primeng/inputtext';
import {PasswordModule} from 'primeng/password';
import {RegisterForm} from "../../../core/type/register-form";

@Component({
  selector: 'app-registration-form',
  imports: [CommonModule,
    ReactiveFormsModule,
    ButtonModule,
    CardModule,
    FloatLabelModule,
    InputGroupModule,
    InputGroupAddonModule,
    InputTextModule,
    PasswordModule],
  templateUrl: './registration-form.html',
  styleUrl: './registration-form.scss',
  standalone: true
})
export class RegistrationForm {

  private formBuilder = inject(FormBuilder);

  submitted = false;
  loading = false;

  registerForm = this.formBuilder.nonNullable.group<RegisterForm>({
    firstname: this.formBuilder.nonNullable.control('', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]),
    lastname: this.formBuilder.nonNullable.control('', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]),
    username: this.formBuilder.nonNullable.control('', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]),
    email: this.formBuilder.nonNullable.control('', [Validators.required, Validators.email]),
    password: this.formBuilder.nonNullable.control(
      '', [
        Validators.required,
        Validators.minLength(12),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).+$/)
      ]),
    confirmPassword: this.formBuilder.nonNullable.control('', Validators.required),
  }, {
    validators: this.passwordMatchValidator.bind(this)
  }) ;

  get form() {
    return this.registerForm.controls;
  }

  private passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const {password, confirmPassword} = control.value;
    return password === confirmPassword ? null : {passwordMismatch: true};
  }

  protected isInvalid(fieldName: string): boolean {
    const field = this.registerForm.get(fieldName);
    return !!(field && field.invalid && (field.touched || this.submitted));
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.registerForm.invalid) {
      return;
    }

    this.loading = true;

    const payload = this.registerForm.getRawValue();
    delete (payload as any).confirmPassword;

    console.log(payload);

    setTimeout(() => {
      this.loading = false;
      this.registerForm.reset();
      this.submitted = false;
    }, 1500);
  };

  onReset(): void {
    this.registerForm.reset();
    this.submitted = false;
    this.loading = false;
  };
}
