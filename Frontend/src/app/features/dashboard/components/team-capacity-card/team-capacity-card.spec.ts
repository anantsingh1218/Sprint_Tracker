import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamCapacityCard } from './team-capacity-card';

describe('TeamCapacityCard', () => {
  let component: TeamCapacityCard;
  let fixture: ComponentFixture<TeamCapacityCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TeamCapacityCard],
    }).compileComponents();

    fixture = TestBed.createComponent(TeamCapacityCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
