import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment.development';
import {map, Observable} from 'rxjs';
import {Review} from '../type/review';
import {ServiceResponse} from '../type/service-response';

@Injectable({
  providedIn: 'root',
})
export class ReviewService {
  private http = inject(HttpClient);
  private readonly API_URL = environment.apiUrl;

  getReviewsByBookId(bookId: number): Observable<Review[]> {
    return this.http.get<ServiceResponse<Review[]>>(`${this.API_URL}/review/${bookId}`)
      .pipe(map(response => response.data ?? []));
  }

  addReview(review: any): Observable<Review> {
    return this.http.post<ServiceResponse<Review>>(`${this.API_URL}/review`, review)
      .pipe(map(response => {
        if (response.code !== '10000') {
          throw new Error(response.code);
        }
        return response.data;
      }));
  }

  updateReview(review: any): Observable<Review> {
    return this.http.put<ServiceResponse<Review>>(`${this.API_URL}/review/update`, review)
      .pipe(map(response => {
        if (response.code !== '10005') {
          throw new Error(response.code);
        }
        return response.data;
      }));
  }

  deleteReview(reviewId: number): Observable<any> {
    return this.http.delete<ServiceResponse<any>>(`${this.API_URL}/review/delete/${reviewId}`)
      .pipe(map(response => {
        if (response.code !== '10010') {
          throw new Error(response.code);
        }
        return response.data;
      }));
  }

}
