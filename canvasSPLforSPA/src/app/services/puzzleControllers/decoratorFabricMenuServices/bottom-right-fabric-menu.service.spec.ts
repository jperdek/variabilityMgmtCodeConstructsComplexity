import { TestBed } from '@angular/core/testing';

import { BottomRightFabricMenuService } from './bottom-right-fabric-menu.service';

describe('BottomRightFabricMenuService', () => {
  let service: BottomRightFabricMenuService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BottomRightFabricMenuService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
