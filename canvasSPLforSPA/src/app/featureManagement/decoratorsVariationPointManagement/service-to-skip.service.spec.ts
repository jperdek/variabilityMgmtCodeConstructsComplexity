import { TestBed } from '@angular/core/testing';

import { ServiceToSkipService } from './service-to-skip.service';

describe('ServiceToSkipService', () => {
  let service: ServiceToSkipService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiceToSkipService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
