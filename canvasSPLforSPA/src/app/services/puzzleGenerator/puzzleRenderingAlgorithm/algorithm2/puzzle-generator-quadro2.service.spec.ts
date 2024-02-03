import { TestBed } from '@angular/core/testing';

import { PuzzleGeneratorQuadroService2 } from './puzzle-generator-quadro2.service';

describe('PuzzleGeneratorQuadroService2', () => {
  let service: PuzzleGeneratorQuadroService2;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PuzzleGeneratorQuadroService2);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
