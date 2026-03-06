import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from './auth.service';
import { NotificationService } from '../notification/notification.service';

/**
 * Functional HTTP interceptor:
 * - Adds Authorization: Bearer <token> header to every outgoing request.
 * - On 401 response, triggers logout and redirect to /login.
 * - On any 4xx/5xx, fires a global error toast via NotificationService.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const notification = inject(NotificationService);
  const token = authService.token();

  // Skip token injection for auth endpoints
  const isAuthUrl =
    req.url.includes('/auth/login') ||
    req.url.includes('/auth/register') ||
    req.url.includes('/auth/refresh-token');

  let request = req;
  if (token && !isAuthUrl) {
    request = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` },
    });
  }

  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && !isAuthUrl) {
        authService.logout();
        // Don't toast on 401 redirect — user is sent to login
      } else if (error.status >= 400) {
        notification.httpError(error);
      }
      return throwError(() => error);
    }),
  );
};

