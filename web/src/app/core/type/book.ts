import { Author } from './author';
import { Genre } from './genre';
import {Publisher} from './publisher';

export interface Book {
  id: number;
  isbn: string;
  title: string;
  author: Author;
  publisher: Publisher;
  year: number;
  genres: Genre[];
  description: string;
  language: string;
  quantity: number;
  first_page_url: string;
  createdAt: Date;
  updatedAt: Date;
  createdBy: number;
  updatedBy: number;
}
