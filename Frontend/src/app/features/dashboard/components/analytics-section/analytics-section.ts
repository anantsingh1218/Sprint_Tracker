import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Velocity } from '../../models/dashboard.model';
import { Burndown } from '../../models/dashboard.model';
import { TeamCapacity } from '../../models/dashboard.model';
import { ReleaseReadiness } from '../../models/dashboard.model';

import { VelocityCard } from '../velocity-card/velocity-card';
import { BurndownCard } from '../burndown-card/burndown-card';
import { ReleaseCard } from '../release-card/release-card';
import { TeamCapacityCard } from '../team-capacity-card/team-capacity-card';

@Component({
  selector: 'app-analytics-section',
  standalone: true,
  imports: [

    CommonModule,

    VelocityCard,

    BurndownCard,

    TeamCapacityCard,

    ReleaseCard

],
  templateUrl: './analytics-section.html',
  styleUrl: './analytics-section.css'
})
export class AnalyticsSection {

  @Input() velocity?: Velocity;

  @Input() burndown?: Burndown;

  @Input() teamCapacity?: TeamCapacity;

  @Input() releaseReadiness?: ReleaseReadiness;

}