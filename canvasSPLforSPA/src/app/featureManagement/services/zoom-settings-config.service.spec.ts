import { TestBed } from '@angular/core/testing';

import { ZoomSettingsConfigService } from './zoom-settings-config.service';

describe('ZoomSettingsConfigService', () => {
  let service: ZoomSettingsConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ZoomSettingsConfigService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
