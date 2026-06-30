import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Story } from '../story/story';
import { IStory } from '../../models/storyInterface';

@Component({
  selector: 'app-story-list',
  standalone: true,
  imports: [CommonModule, FormsModule, Story],
  templateUrl: './story-list.html',
  styleUrl: './story-list.css',
})
export class StoryList {
  isStoryOpen = false;

  selectedStory!: IStory;

  stories: IStory[] = [
  {
    id: 'S101',
    title: 'Login Page UI',
    body: 'Build login screen with validation',

    status: 'todo',
    priority: 'Medium',
    estimatedStoryPoints: 8,
    remainingStoryPoint: 5,

    featureId: 0,
    sprintId: 0,
    userId: 0,

    comments: [
      {
        userId: 0,
        text: 'Story created',
        createdAt: new Date().toISOString()
      }
    ]
  },

  {
    id: 'S102',
    title: 'Sprint API Integration',
    body: 'Connect sprint module to backend',

    status: 'in-progress',
    priority: 'High',
    estimatedStoryPoints: 8,
    remainingStoryPoint: 5,

    featureId: 1,
    sprintId: 3,
    userId: 1,

    comments: [
      {
        userId: 1,
        text: 'Working on API contract',
        createdAt: new Date().toISOString()
      }
    ]
  }
  ];
  openStory(story: IStory) {
    this.selectedStory = { ...story };
    this.isStoryOpen = true;
  }

  closeStory() {
    this.isStoryOpen = false;
  }

  openCreateStory() {
  this.selectedStory = {
    id: 'S' + (this.stories.length + 1),
    title: '',
    body: '',

    status: 'todo',
    priority: 'Medium',
    estimatedStoryPoints: 0,
    remainingStoryPoint: 0,

    featureId: null,
    sprintId: null,
    userId: null,

    comments: []
  };

  this.isStoryOpen = true;
}

  saveStory(updated: IStory) {
    const index = this.stories.findIndex((s) => s.id === updated.id);

    if (index >= 0) {
      this.stories[index] = updated;
    } else {
      this.stories.push(updated);
    }

    this.closeStory();
  }
}
