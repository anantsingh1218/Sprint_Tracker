import { Component, OnInit,ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FeatureOverlay } from '../feature-overlay/feature-overlay';
import { IFeature } from '../../models/featureInterface';
import { Priority, WorkStatus } from '../../models/workItem';
import { FeatureService } from './feature.service';

@Component({
  selector: 'app-feature-list',
  standalone: true,
  imports: [CommonModule, FormsModule, FeatureOverlay],
  templateUrl: './feature-list.html',
  styleUrl: './feature-list.css',
})
export class FeatureList implements OnInit {
  isFeatureOpen = false;

  selectedFeature!: IFeature;

  features: IFeature[] = [];

  constructor(
    private featureService: FeatureService,
    private route: ActivatedRoute,
    private cdr:ChangeDetectorRef
  ) {}



  ngOnInit() {
    this.loadFeatures();

    this.route.paramMap.subscribe(params => {
      this.checkAndOpenId(params.get('id'));
    });
  }

  loadFeatures() {
    this.featureService.getAllFeatures().subscribe({
      next: (data) => {
        this.features = data;
        this.checkAndOpenId(this.route.snapshot.paramMap.get('id'));
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error fetching features', err)
    });
  }

  checkAndOpenId(id: string | null) {
    if (id && this.features.length > 0) {
      const feature = this.features.find(f => f.id?.toString() === id);
      if (feature) {
        this.openFeature(feature);
      }
    }
  }


  openFeature(feature: IFeature) {
    this.selectedFeature = { ...feature };
    this.isFeatureOpen = true;
  }

  closeFeature() {
    this.isFeatureOpen = false;
  }

  openCreateFeature() {
    this.selectedFeature = {
      title: '',
      description: '',
      featureStatus: WorkStatus.OPEN,
      priority: Priority.LOW,
      estimatedStoryPoints: 0,
      remainingStoryPoints: 0,
      productName: null,
      sprintName: null,
      assignedTo: null,
      commentsList: [],
    };

    this.isFeatureOpen = true;
  }

  saveFeature(updated: IFeature) {
    const index = this.features.findIndex((s) => s.featureCode === updated.featureCode);

    if (index >= 0) {
      this.features[index] = updated;
    } else {
      this.features.push(updated);
    }

    this.closeFeature();
  }
}
