import { FormControl } from '@angular/forms';

export type PasswordForm = {
  currentPassword: FormControl<string>;
  newPassword: FormControl<string>;
  confirmPassword: FormControl<string>;
};
