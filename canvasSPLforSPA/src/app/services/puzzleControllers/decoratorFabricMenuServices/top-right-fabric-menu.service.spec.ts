import { TestBed } from '@angular/core/testing';

import { TopRightFabricMenuService } from './top-right-fabric-menu.service';

describe('TopRightFabricMenuService', () => {
  let service: TopRightFabricMenuService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TopRightFabricMenuService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
