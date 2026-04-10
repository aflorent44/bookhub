import {Country} from './country';

export interface Author {
  id: number;
  firstName: string;
  lastName: string;
  country: Country;
  createdAt: Date;
  updatedAt: Date;
  createdBy: number;
  updatedBy: number;
}
