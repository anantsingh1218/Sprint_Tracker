import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { FeatureOverlay } from '../feature-overlay/feature-overlay';
import { IFeature } from '../../models/featureInterface';

@Component({
  selector: 'app-feature-list',
  standalone: true,
  imports: [CommonModule, FormsModule, FeatureOverlay],
  templateUrl: './feature-list.html',
  styleUrl: './feature-list.css',
})
export class FeatureList {
  isFeatureOpen = false;

  selectedFeature!: IFeature;

  features: IFeature[] = [
    {
      id: 'F101',
      title: 'Login Page UI',
      description: 'Build login screen with validation',

      status: 'todo',
      priority: 'Medium',
      estimatedStoryPoints: 8,
      remainingStoryPoint: 5,

      productId: 0,
      sprintId: 0,
      userId: 0,

      comments: [
        {
          userId: 0,
          text: 'Feature created',
          createdAt: new Date().toISOString(),
        },
      ],
    },

    {
      id: 'F102',
      title: 'Sprint API Integration',
      description: 'Connect sprint module to backend',

      status: 'in-progress',
      priority: 'High',
      estimatedStoryPoints: 8,
      remainingStoryPoint: 5,

      productId: 1,
      sprintId: 3,
      userId: 1,

      comments: [
        {
          userId: 1,
          text: 'Working on API contract',
          createdAt: new Date().toISOString(),
        },
      ],
    },
  ];
  openFeature(feature: IFeature) {
    this.selectedFeature = { ...feature };
    this.isFeatureOpen = true;
  }

  closeFeature() {
    this.isFeatureOpen = false;
  }

  openCreateFeature() {
    this.selectedFeature = {
      id: 'F' + this.features.length + 1,
      title: '',
      description: '',

      status: 'todo',
      priority: 'Medium',
      estimatedStoryPoints: 0,
      remainingStoryPoint: 0,

      productId: null,
      sprintId: null,
      userId: null,

      comments: [],
    };

    this.isFeatureOpen = true;
  }

  saveFeature(updated: IFeature) {
    const index = this.features.findIndex((s) => s.id === updated.id);

    if (index >= 0) {
      this.features[index] = updated;
    } else {
      this.features.push(updated);
    }

    this.closeFeature();
  }
}
