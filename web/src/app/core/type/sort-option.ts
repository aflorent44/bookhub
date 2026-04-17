import { SortDirection } from "./sort-direction";
import { SortField } from "./sort-field";

export interface SortOption {
  label: string;
  field: SortField;
  direction: SortDirection;
}
