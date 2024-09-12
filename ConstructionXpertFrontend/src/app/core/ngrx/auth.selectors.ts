import { createSelector } from '@ngrx/store';
import { AppState } from './app.state';

export const selectAuthState = (state: AppState) => state.auth;

export const selectUser = createSelector(
  selectAuthState,
  (state) => state.user
);

export const selectRole = createSelector(
  selectAuthState,
  (state) => state.role
);
