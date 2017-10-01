import { TestBed, async, inject } from '@angular/core/testing';

import { LoginedGuard } from './logined.guard';

describe('LoginedGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LoginedGuard]
    });
  });

  it('should ...', inject([LoginedGuard], (guard: LoginedGuard) => {
    expect(guard).toBeTruthy();
  }));
});
