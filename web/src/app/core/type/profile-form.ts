import { FormControl } from '@angular/forms';

export type ProfileForm = {
  firstName: FormControl<string>;
  lastName: FormControl<string>;
  phoneNumber: FormControl<string>;
};
