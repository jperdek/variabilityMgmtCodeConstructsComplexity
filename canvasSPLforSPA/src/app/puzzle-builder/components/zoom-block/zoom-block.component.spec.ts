import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ZoomBlockComponent } from './zoom-block.component';

describe('ZoomBlockComponent', () => {
  let component: ZoomBlockComponent;
  let fixture: ComponentFixture<ZoomBlockComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ZoomBlockComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ZoomBlockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
