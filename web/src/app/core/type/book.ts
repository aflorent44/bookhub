import { Author } from './author';
import { Genre } from './genre';
import { Publisher } from './publisher';
import { User } from './user';

// export interface Book {
//   id: number;        // Integer en Java, pas string
//   isbn: string;
//   title: string;
//   author: Author;
//   publisher: Publisher;
//   year: number;
//   genres: Genre[];   // Set en Java → Array en TS
//   description: string;
//   language?: string; // pas dans l'entité Java, à garder optionnel
//   quantity: number;
//   first_page_url?: string;  // firstPageUrl en Java (camelCase)
//   createdAt: Date;
//   updatedAt: Date;
//   createdBy: User;   // c'est un User en Java, pas un number
//   updatedBy: User;   // idem
// }

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
  first_page_url?: string;
  createdAt?: string;
  updatedAt?: string;
  createdBy?: number;
  updatedBy?: number;
}
