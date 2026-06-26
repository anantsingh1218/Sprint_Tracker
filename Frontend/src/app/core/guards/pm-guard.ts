import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { AuthService } from '../auth/auth';
import { SystemService } from '../../services/systemService';
import { firstValueFrom } from 'rxjs';

export const pmGuard: CanActivateFn = async () => {

  const authService = inject(AuthService);
  const systemService = inject(SystemService);
  const router = inject(Router);

  const role = authService.getRole();

  const system = await firstValueFrom(systemService.getStatus());

  if (!system.initialized) {
    return true;
  }

  if (role?.endsWith('PM')) {
    return true;
  }

  router.navigate(['/dashboard']);
  return false;
};