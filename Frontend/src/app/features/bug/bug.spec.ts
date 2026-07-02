import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Bug } from './bug';

describe('Bug', () => {
  let component: Bug;
  let fixture: ComponentFixture<Bug>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Bug],
    }).compileComponents();

    fixture = TestBed.createComponent(Bug);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
