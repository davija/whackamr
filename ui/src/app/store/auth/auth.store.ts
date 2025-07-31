import { computed } from '@angular/core';
import { UserDto } from '@models/app.model';
import { patchState, signalStore, withComputed, withMethods, withState } from '@ngrx/signals';

export interface AuthState {
  token: string | undefined;
  refreshToken: string | undefined;
  currentUser: UserDto | undefined;
  permissions: string[];
  loading: boolean;
}

const initialState: AuthState = {
  token: undefined,
  refreshToken: undefined,
  currentUser: undefined,
  permissions: [],
  loading: false,
};

export const AuthStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withComputed(({ token }) => ({
    isAuthenticated: computed(() => {
      const result = token();

      console.log('authentication status: ' + result);

      return result;
    }),
  })),
  withMethods(store => ({
    updateToken(token: string): void {
      patchState(store, state => ({ ...state, token }));
    },
    updateRefreshToken(refreshToken: string): void {
      patchState(store, state => ({ ...state, refreshToken }));
    },
    setLoading(loading: boolean) {
      patchState(store, state => ({ ...state, loading }));
    },
    logout(): void {
      patchState(store, () => initialState);
    },
    login(token: string, refreshToken: string): void {
      patchState(store, () => ({ token, refreshToken, loading: false }));
    },
    setPermissions(permissions: string[]): void {
      patchState(store, state => ({ ...state, permissions }));
    },
    setCurrentUser(currentUser: UserDto): void {
      patchState(store, state => ({ ...state, currentUser }));
    },
    hasPermission(permission: string): boolean {
      return store.permissions().includes(permission);
    },
  })),
);

export type AuthStore = InstanceType<typeof AuthStore>;
