import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Bug } from '../bug/bug';
import { IBug } from '../../models/bugInterface';
import { Priority, WorkStatus } from '../../models/workItem';

@Component({
  selector: 'app-story-list',
  standalone: true,
  imports: [CommonModule, FormsModule, Bug],
  templateUrl: './bug-list.html',
  styleUrl: './bug-list.css',
})
export class BugList {
  isBugOpen = false;

  selectedBug!: IBug;

  bugs: IBug[] = [];
  openBug(bug: IBug) {
    this.selectedBug = { ...bug };
    this.isBugOpen = true;
  }

  closeBug() {
    this.isBugOpen = false;
  }

  openCreateBug() {
  this.selectedBug = {
    id: 'B' + (this.bugs.length + 1),
    title: '',
    description: '',

    status: WorkStatus.OPEN,
    priority: Priority.LOW,
    estimatedHours: 0,
    remainingHours: 0,

    storyCode: null,
    sprintCode: null,
    assignedTo: null,
    reopenCount: 0,

    comments: []
  };

  this.isBugOpen = true;
}

  saveBug(updated: IBug) {
    const index = this.bugs.findIndex((b) => b.id === updated.id);

    if (index >= 0) {
      this.bugs[index] = updated;
    } else {
      this.bugs.push(updated);
    }

    this.closeBug();
  }
}
