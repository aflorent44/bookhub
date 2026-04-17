export interface Review {
  reviewId?: number;
  bookId: number;
  user?: UserBasicResponse;
  rating: number;
  comment: string;
  isHidden?: boolean;
  hiddenBy?: UserBasicResponse;
  createdAt?: string;
  updatedAt?: string;
  updatedBy?: UserBasicResponse;
}

export interface UserBasicResponse {
  id: number;
  username: string;
  firstName?: string;
  lastName?: string;
}
