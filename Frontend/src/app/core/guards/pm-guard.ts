import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { AuthService } from '../auth/auth';

export const pmGuard: CanActivateFn = () => {

  const authService = inject(AuthService);
  const router = inject(Router);

  const role = authService.getRole();

  if (role?.endsWith('PM')) {
    return true;
  }

  router.navigate(['/dashboard']);
  return false;
};