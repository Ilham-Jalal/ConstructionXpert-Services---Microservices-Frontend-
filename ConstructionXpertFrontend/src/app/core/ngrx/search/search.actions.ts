import { createAction, props } from '@ngrx/store';

export const updateSearchTerm = createAction(
  '[Search] Update Search Term',
  props<{ searchTerm: string }>()
);
