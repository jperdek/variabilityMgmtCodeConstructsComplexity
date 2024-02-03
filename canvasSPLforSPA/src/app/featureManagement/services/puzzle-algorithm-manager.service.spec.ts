import { TestBed } from '@angular/core/testing';

import { PuzzleAlgorithmManagerService } from './puzzle-algorithm-manager.service';

describe('PuzzleAlgorithmManagerService', () => {
  let service: PuzzleAlgorithmManagerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PuzzleAlgorithmManagerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
