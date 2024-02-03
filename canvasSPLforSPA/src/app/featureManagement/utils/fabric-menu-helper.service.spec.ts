import { TestBed } from '@angular/core/testing';

import { FabricMenuHelperService } from './fabric-menu-helper.service';

describe('FabricMenuHelperService', () => {
  let service: FabricMenuHelperService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FabricMenuHelperService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
