import {ReservationStates} from './reservation-states';

export interface Reservation {
  id: number;
  book: { id: number; title: string; author: string; coverUrl: string; };
  queuePosition: number;
  status: ReservationStates;
  createdAt: string;
}
