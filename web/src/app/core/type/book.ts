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
  first_page_url?: string | null;
  createdAt?: string;
  updatedAt?: string;
  createdBy?: string;
  updatedBy?: string;
}
