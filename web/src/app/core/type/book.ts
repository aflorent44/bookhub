import { Genre } from './genre';

export interface Book {
  id: number;
  isbn: string;
  title: string;
  year: number;
  quantity: number;
  description: string;
  authorFirstName: string;
  authorLastName: string;
  publisherName: string;
  genres: Genre[];
  firstPageUrl?: string | null;
  createdAt?: string;
  updatedAt?: string;
  createdBy?: string;
  updatedBy?: string;
}
