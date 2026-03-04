import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

/**
 * Route guard — redirects to /login if user is not authenticated.
 */
export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    return true;
  }

  router.navigate(['/login']);
  return false;
};

/**
 * Route guard — checks if user has at least one of the required roles.
 * Usage in route: canActivate: [roleGuard], data: { roles: ['ROLE_ADMIN'] }
 */
export const roleGuard: CanActivateFn = (route) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isLoggedIn()) {
    router.navigate(['/login']);
    return false;
  }

  const requiredRoles: string[] = route.data?.['roles'] ?? [];
  if (requiredRoles.length === 0) {
    return true;
  }

  if (authService.hasAnyRole(...requiredRoles)) {
    return true;
  }

  // Unauthorized — redirect to dashboard
  router.navigate(['/dashboard']);
  return false;
};

