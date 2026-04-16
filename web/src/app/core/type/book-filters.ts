import { SortDirection } from "./sort-direction";
import { SortField } from "./sort-field";

export interface BookFilters {
  keyword?: string;
  title?: string;
  year?: number;
  isbn?: string;
  quantity?: string;
  authorFirstName?: string;
  authorLastName?: string;
  genre?: string;
  publisher?: string;
  countryName?: string;
  countryNationality?: string;
  page: number;
  size: number;
  sortBy: SortField | null;
  sortDirection: SortDirection | null;
}

export const DEFAULT_FILTERS: BookFilters = {
  keyword: '',
  page: 0,
  size: 15,
  sortBy: null,
  sortDirection: null,
};
