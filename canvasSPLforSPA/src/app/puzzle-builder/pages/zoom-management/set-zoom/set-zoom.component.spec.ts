import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SetZoomComponent } from './set-zoom.component';

describe('SetZoomComponent', () => {
  let component: SetZoomComponent;
  let fixture: ComponentFixture<SetZoomComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SetZoomComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SetZoomComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
