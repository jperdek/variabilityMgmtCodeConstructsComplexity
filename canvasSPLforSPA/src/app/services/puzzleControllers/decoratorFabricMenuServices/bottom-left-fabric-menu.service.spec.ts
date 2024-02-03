import { TestBed } from '@angular/core/testing';

import { BottomLeftFabricMenuService } from './bottom-left-fabric-menu.service';

describe('BottomLeftFabricMenuService', () => {
  let service: BottomLeftFabricMenuService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BottomLeftFabricMenuService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
