import { Pipe, PipeTransform } from '@angular/core';
import { LoanStatus } from '../type/loan';

@Pipe({
  name: 'loanStatus',
  standalone: true
})
export class LoanStatusPipe implements PipeTransform {
  transform(status: LoanStatus): string {
    const labels: Record<LoanStatus, string> = {
      'IN_PROGRESS': 'En cours',
      'WAITING':     'En attente',
      'FINISHED':    'Retourné',
      'LATE':        'En retard',
    };
    return labels[status] ?? status;
  }
}
