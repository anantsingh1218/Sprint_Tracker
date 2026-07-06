import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnalyticsSection } from './analytics-section';

describe('AnalyticsSection', () => {
  let component: AnalyticsSection;
  let fixture: ComponentFixture<AnalyticsSection>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AnalyticsSection],
    }).compileComponents();

    fixture = TestBed.createComponent(AnalyticsSection);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
