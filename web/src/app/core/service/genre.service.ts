import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment.development';
import {map, Observable} from 'rxjs';
import {Genre} from '../type/genre';
import {ServiceResponse} from '../type/service-response';

@Injectable({
  providedIn: 'root',
})
export class GenreService {

  private http = inject(HttpClient);
  private apiUrl : string = environment.apiUrl;

  getGenres(): Observable<Genre[]> {
    return this.http.get<ServiceResponse<Genre[]>>(`${this.apiUrl}/genres`)
      .pipe(map((response: ServiceResponse<Genre[]>) => response.data));
  }

  createGenre(genre: Genre): Observable<Genre> {
    return this.http.post<ServiceResponse<Genre>>(`${this.apiUrl}/genres`, genre)
      .pipe(map((response: ServiceResponse<Genre>) => response.data));
  }

}
