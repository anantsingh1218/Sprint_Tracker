import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { TeamCapacity } from '../../models/dashboard.model';

@Component({
  selector: 'app-team-capacity-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './team-capacity-card.html',
  styleUrl: './team-capacity-card.css'
})
export class TeamCapacityCard {

  @Input()
  teamCapacity!: TeamCapacity;

}