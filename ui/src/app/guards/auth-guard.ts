import { Injectable } from '@angular/core';
import { CanActivate, CanActivateChild, GuardResult, MaybeAsync, Router } from '@angular/router';
import { AuthStore } from '@store/auth/auth.store';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate, CanActivateChild {
  constructor(
    private authStore: AuthStore,
    private router: Router,
  ) {}

  canActivate(): MaybeAsync<GuardResult> {
    return this.checkAuth();
  }

  canActivateChild(): MaybeAsync<GuardResult> {
    return this.checkAuth();
  }

  private checkAuth(): boolean {
    if (this.authStore.isAuthenticated()) {
      return true;
    } else {
      this.router.navigate(['/login']);

      return false;
    }
  }
}
