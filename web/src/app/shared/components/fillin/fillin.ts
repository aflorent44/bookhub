import { Component, Input, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { TooltipModule } from 'primeng/tooltip';

@Component({
  selector: 'app-fillin',
  templateUrl: './fillin.html',
  styleUrls: ['./fillin.scss'],
  imports: [TooltipModule],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => Fillin),
      multi: true
    }
  ]
})
export class Fillin implements ControlValueAccessor {

  @Input() title: string = '';
  @Input() tooltip: string = '';
  @Input() placeholder: string = '';

  value: string = '';

  private onChange = (value: string) => {};
  private onTouched = () => {};

  writeValue(value: string): void {
    this.value = value ?? '';
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
  }

  updateValue(event: Event): void {
    const input = event.target as HTMLTextAreaElement;
    this.value = input.value;
    this.onChange(this.value);
  }

  markTouched(): void {
    this.onTouched();
  }
}