import {User} from './user';

export interface UserResponse extends User {
  firstname: string;
  lastname: string;
  username: string;
  createdAt: string;
  phoneNumber?: string;
}
