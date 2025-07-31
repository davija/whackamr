/* eslint-disable @typescript-eslint/no-explicit-any */
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable, Injector } from '@angular/core';
import { AuthService } from '@services/auth.service';
import { AuthStore } from '@store/auth/auth.store';
import { BehaviorSubject, catchError, filter, Observable, switchMap, take, throwError } from 'rxjs';

const TOKEN_HEADER_KEY = 'Authorization';

@Injectable({
  providedIn: 'root',
})
export class AuthTokenInterceptor implements HttpInterceptor {
  private authEndpoints = ['auth/login'];

  private isRefreshing = false;
  private refreshTokenSubject = new BehaviorSubject<any>(null);

  constructor(private injector: Injector) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const authService = this.injector.get(AuthService);
    const authStore = this.injector.get(AuthStore);
    const authRequest = this.processRequest(req, authStore);

    return next.handle(authRequest).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status == 401 && this.shouldRetryUrl(authRequest.url)) {
          return this.handle401Error(authRequest, next, authService, authStore);
        }

        return throwError(() => error);
      }),
    );
  }

  private handle401Error(
    request: HttpRequest<any>,
    next: HttpHandler,
    authService: AuthService,
    authStore: AuthStore,
  ): Observable<HttpEvent<any>> {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      return authService.refreshToken().pipe(
        switchMap(() => {
          const token = authStore.token();

          this.isRefreshing = false;
          this.refreshTokenSubject.next(token);

          return next.handle(this.processRequest(request, authStore));
        }),
        catchError(error => {
          this.isRefreshing = false;
          authStore.logout();

          return throwError(() => error);
        }),
      );
    }

    return this.refreshTokenSubject.pipe(
      filter(token => token !== null),
      take(1),
      switchMap(() => next.handle(this.processRequest(request, authStore))),
    );
  }

  private processRequest(request: HttpRequest<unknown>, authStore: AuthStore) {
    const token = authStore.token();

    return token ? request.clone({ headers: request.headers.set(TOKEN_HEADER_KEY, `Bearer ${token}`) }) : request;
  }

  private shouldRetryUrl(url: string): boolean {
    return !this.authEndpoints.some(endpoint => url.includes(endpoint));
  }
}
