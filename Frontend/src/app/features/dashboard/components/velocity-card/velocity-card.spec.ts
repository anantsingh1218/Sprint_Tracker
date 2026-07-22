import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VelocityCard } from './velocity-card';

describe('VelocityCard', () => {
  let component: VelocityCard;
  let fixture: ComponentFixture<VelocityCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VelocityCard],
    }).compileComponents();

    fixture = TestBed.createComponent(VelocityCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
