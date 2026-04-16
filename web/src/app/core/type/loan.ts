export interface Loan {
  id: number;
  debutDate: string;
  endDate: string;
  returnDate?: string | null;
  status: LoanStatus;
  late: boolean;
  userId: number;
  bookId: number;
  bookTitle?: string;
  bookAuthor?: string;
  bookCoverUrl?: string;
  createdAt?: string;
  updatedAt?: string;
  updatedBy?: number;
}

export type LoanStatus = 'IN_PROGRESS' | 'WAITING' | 'FINISHED' | 'LATE';


