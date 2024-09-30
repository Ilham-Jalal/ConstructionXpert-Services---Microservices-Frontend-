import { ActionReducerMap, MetaReducer } from '@ngrx/store';
import { authReducer, AuthState } from './auth/auth.reducer';
import { searchReducer, SearchState } from './search/search.reducer';

export interface AppState {
  auth: AuthState;
  search: SearchState
}

export const reducers: ActionReducerMap<AppState> = {
  auth: authReducer,
  search: searchReducer
};

export const metaReducers: MetaReducer<AppState>[] = [];
