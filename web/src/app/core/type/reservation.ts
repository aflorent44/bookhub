import { Book } from "./book";
import {ReservationStates} from './reservation-states';

export interface Reservation {
  id: number;
  book: Book;
  queuePosition: number;
  status: ReservationStates;
  createdAt: string;
}
