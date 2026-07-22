import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeatureOverlay } from './feature-overlay';

describe('FeatureOverlay', () => {
  let component: FeatureOverlay;
  let fixture: ComponentFixture<FeatureOverlay>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeatureOverlay],
    }).compileComponents();

    fixture = TestBed.createComponent(FeatureOverlay);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
