import { TestBed } from '@angular/core/testing';

import { TopLeftFabricMenuService } from './top-left-fabric-menu.service';

describe('TopLeftFabricMenuService', () => {
  let service: TopLeftFabricMenuService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TopLeftFabricMenuService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
