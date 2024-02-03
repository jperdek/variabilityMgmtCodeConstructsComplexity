import { TestBed } from '@angular/core/testing';

import { VariableSettingsConfigService } from './variable-settings-config.service';

describe('VariableSettingsConfigService', () => {
  let service: VariableSettingsConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VariableSettingsConfigService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
