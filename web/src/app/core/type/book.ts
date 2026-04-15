import { Genre } from './genre';
import {Country} from './country';
import {User} from './user';

export interface Book {
  bookId: number;
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
  createdBy?: User;
  updatedBy?: User;
  country: Country;
}
