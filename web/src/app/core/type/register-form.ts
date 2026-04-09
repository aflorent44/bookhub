import { FormControl } from '@angular/forms';

export interface RegisterForm {
  firstname: FormControl<string>;
  lastname: FormControl<string>;
  username: FormControl<string>;
  email: FormControl<string>;
  password: FormControl<string>;
  confirmPassword: FormControl<string>;
}
