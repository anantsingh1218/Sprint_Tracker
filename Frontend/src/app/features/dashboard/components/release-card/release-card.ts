import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { ReleaseReadiness } from '../../models/dashboard.model';

@Component({
  selector: 'app-release-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './release-card.html',
  styleUrl: './release-card.css'
})
export class ReleaseCard {

  @Input()
  releaseReadiness!: ReleaseReadiness;

}