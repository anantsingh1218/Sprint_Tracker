import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-velocity-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './velocity-card.html',
  styleUrl: './velocity-card.css'
})
export class VelocityCard {

  @Input() velocity: any;

}