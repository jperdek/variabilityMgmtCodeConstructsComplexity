import { TestBed } from '@angular/core/testing';

import { DecoratorTypesService } from './decorator-types.service';

describe('DecoratorTypesService', () => {
  let service: DecoratorTypesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DecoratorTypesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
