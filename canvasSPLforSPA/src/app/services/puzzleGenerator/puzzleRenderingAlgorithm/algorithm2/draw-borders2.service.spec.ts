import { TestBed } from '@angular/core/testing';

import { DrawBordersService2 } from './draw-borders2.service';

describe('DrawBordersService2', () => {
  let service: DrawBordersService2;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DrawBordersService2);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
