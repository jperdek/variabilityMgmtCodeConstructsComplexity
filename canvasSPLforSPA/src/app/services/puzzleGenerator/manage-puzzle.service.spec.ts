import { TestBed } from '@angular/core/testing';

import { ManageGraphicsService } from './manage-puzzle.service';

describe('ManageGraphicsService', () => {
  let service: ManageGraphicsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ManageGraphicsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
