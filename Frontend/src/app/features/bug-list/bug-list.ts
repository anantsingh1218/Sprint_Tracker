import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BugService } from '../bug/bug.service';
import { Bug } from '../bug/bug';
import { IBug } from '../../models/bugInterface';
import { Priority, WorkStatus } from '../../models/workItem';

@Component({
  selector: 'app-bug-list',
  standalone: true,
  imports: [CommonModule, Bug],
  templateUrl: './bug-list.html',
  styleUrl: './bug-list.css'
})
export class BugList implements OnInit {

  bugs: any[] = [];
  isBugOpen = false;
  selectedBug: any;

  constructor(private bugService: BugService) {}

  bugs: IBug[] = [
  {
    id: 'B101',
    title: 'Login Page UI',
    description: 'Build login screen with validation',

    status: WorkStatus.OPEN,
    priority: Priority.MEDIUM,
    estimatedHours: 8,
    remainingHours: 5,

    storyId: 0,
    sprintId: 'Sprint 1',
    assignedTo: 'User 1',
    reopenCount: 0,

    comments: [
      {
        userId: 0,
        text: 'Story created',
        createdAt: new Date().toISOString()
      }
    ]
  },

  {
    id: 'B102',
    title: 'Sprint API Integration',
    description: 'Connect sprint module to backend',

    status: WorkStatus.IN_PROGRESS,
    priority: Priority.HIGH,
    estimatedHours: 8,
    remainingHours: 5,

    storyId: 1,
    sprintId: 'Sprint 2',
    assignedTo: 'User 3',
    reopenCount: 2,

    comments: [
      {
        userId: 1,
        text: 'Working on API contract',
        createdAt: new Date().toISOString()
      }
    ]
  }

  loadBugs() {
    this.bugService.getAllBugs().subscribe({
      next: (res: any) => {
        console.log("Fetched Bugs:", res);
        this.bugs = res;
      },

      error: (err: any) => {
        console.error("Fetch Error:", err);
      }
    });
  }

  openBug(bug: any) {
    this.selectedBug = { ...bug };
    this.isBugOpen = true;
  }

  openCreateBug() {
   this.selectedBug = {
  title: '',
  description: '',
  bugstatus: '',
  priority: '',
  assignedto: 0,
  sprintid: 0,
  storyid: 0,
  originalestimatehours: 0,
  remainingestimatehours: 0,
  comments: ''
};
    this.isBugOpen = true;
  }

  saveBug(bug: any) {
    this.bugService.createBug(bug).subscribe({
      next: (res: any) => {
        console.log("Bug Created:", res);
        this.loadBugs();
        this.closeBug();
      },

      error: (err: any) => {
        console.error("Create Error:", err);
      }
    });
  }

  deleteBug(id: number) {
    this.bugService.deleteBug(id).subscribe({
      next: () => {
        this.loadBugs();
      },

      error: (err: any) => {
        console.error("Delete Error:", err);
      }
    });
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

    storyId: null,
    sprintId: null,
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
