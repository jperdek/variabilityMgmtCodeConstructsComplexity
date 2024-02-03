import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SetZoomPositionComponent } from './set-zoom-position.component';

describe('SetZoomPositionComponent', () => {
  let component: SetZoomPositionComponent;
  let fixture: ComponentFixture<SetZoomPositionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SetZoomPositionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SetZoomPositionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
