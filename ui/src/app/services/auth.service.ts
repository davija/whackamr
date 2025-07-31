import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { LoginResponse, TokenResponse } from '@models/app.model';
import { AuthStore } from '@store/auth/auth.store';
import { catchError, map, Observable, of, take } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(
    private http: HttpClient,
    private authStore: AuthStore,
    private router: Router,
    private jwtHelper: JwtHelperService,
    @Inject('API_BASE_URL') private apiBaseUrl: string,
  ) {}

  public logout() {
    this.authStore.logout();
    this.router.navigate(['/login']);
  }

  public login(username: string, password: string): Observable<boolean> {
    const url = `${this.apiBaseUrl}auth/login`;
    const request = {
      username,
      password,
    };

    this.authStore.setLoading(true);

    return this.http.post<LoginResponse>(url, request).pipe(
      take(1),
      map(response => {
        if (response) {
          this.authStore.login(response.accessToken, response.refreshToken);

          return true;
        } else {
          this.authStore.logout();

          return false;
        }
      }),
      catchError(error => {
        console.error('Caught error while trying to login', error);
        this.authStore.logout();

        return of(false);
      }),
    );
  }

  public refreshToken(): Observable<boolean> {
    const url = `${this.apiBaseUrl}auth/refresh-token`;
    const refreshToken = this.authStore.refreshToken();
    const headers = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };

    if (refreshToken && !this.jwtHelper.isTokenExpired(refreshToken)) {
      return this.http.post<TokenResponse>(url, { refreshToken }, headers).pipe(
        take(1),
        map(tokenResponse => {
          this.authStore.login(tokenResponse.accessToken, tokenResponse.refreshToken);

          return true;
        }),
        catchError(() => {
          // Error refreshing token, log user out.
          this.logout();

          return of(false);
        }),
      );
    }

    // Token was not valid, log user out.
    this.authStore.logout();

    return of(false);
  }
}
