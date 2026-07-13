import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntegratedView } from './integrated-view';

describe('IntegratedView', () => {
  let component: IntegratedView;
  let fixture: ComponentFixture<IntegratedView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IntegratedView],
    }).compileComponents();

    fixture = TestBed.createComponent(IntegratedView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
