import { createSelector, createFeatureSelector } from '@ngrx/store';
import { SearchState } from './search.reducer';

export const selectSearchState = createFeatureSelector<SearchState>('search');

export const selectSearchTerm = createSelector(
  selectSearchState,
  (state: SearchState) => state.searchTerm
);
