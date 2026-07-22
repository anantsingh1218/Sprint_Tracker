import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { pmGuard } from './pm-guard';

describe('pmGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) =>
    TestBed.runInInjectionContext(() => pmGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
