import { Author } from './author';
import { Genre } from './genre';

export interface Book {
  id: number;
  isbn: string;
  title: string;
  author: Author;
  publisher: string;
  year: number;
  genres: Genre[];
  synopsis: string;
  language: string;
  quantity: number;
  createdAt: Date;
  updatedAt: Date;
  createdBy: number;
  updatedBy: number;
}
