import {UserResponse} from './user-response';

export interface AuthResponse {
  token: string;
  tokenType: string;
  user: UserResponse;
}
