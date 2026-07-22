import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BurndownCard } from './burndown-card';

describe('BurndownCard', () => {
  let component: BurndownCard;
  let fixture: ComponentFixture<BurndownCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BurndownCard],
    }).compileComponents();

    fixture = TestBed.createComponent(BurndownCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
