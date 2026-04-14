export interface Loan {
  id: number;
  debutDate: string;
  endDate: string;
  returnDate?: string | null;
  status: LoanStatus;
  userId: number;
  bookId: number;
  createdAt?: string;
  updatedAt?: string;
  updatedBy?: number;
}

export type LoanStatus = 'IN_PROGRESS' | 'WAITING' | 'RETURNED' | 'LATE';


