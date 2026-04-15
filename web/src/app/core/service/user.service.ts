import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment.development';
import { map, Observable } from 'rxjs';
import { ServiceResponse } from '../type/service-response';
import {UserResponse} from '../type/user-response';

@Injectable({ providedIn: 'root' })
export class UserService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  getUserById(id: number): Observable<UserResponse> {
    return this.http.get<ServiceResponse<UserResponse>>(`${this.apiUrl}/users/${id}`)
      .pipe(map(response => response.data));
  }
}
