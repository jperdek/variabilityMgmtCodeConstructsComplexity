import { TestBed } from '@angular/core/testing';

import { AnnotationExtractorService } from './annotation-extractor.service';

describe('AnnotationExtractorService', () => {
  let service: AnnotationExtractorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AnnotationExtractorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
