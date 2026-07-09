import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

import { SprintService } from './sprint.service';
import { TaskService } from '../tasks/task.service';

interface SprintData {
  id?: string;
  sprintName: string;
  description: string;
  productCode: string | null;
  startDate: string;
  endDate: string;
  sprintDuration: number;
  status: string;
}

@Component({
  selector: 'app-sprint',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './sprint.html',
  styleUrl: './sprint.css',
})
export class Sprint implements OnInit {
  sprints: SprintData[] = [];
  selectedSprint: SprintData | null = null;
  showSprintModal = false;
  allTasks: any[] = [];

  newSprint: SprintData = {
    sprintName: '',
    description: '',
    productCode: null,
    startDate: '',
    endDate: '',
    sprintDuration: 0,
    status: 'Planned',
  };

  constructor(
    private sprintService: SprintService,
    private taskService: TaskService,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef // Added to force UI rendering on async data receipt
  ) {}

  ngOnInit(): void {
    // 1. Initial load of all sprints and tasks
    this.loadSprints();
    this.loadTasks();

    // 2. Watch URL route parameters for changes cleanly
    this.route.paramMap.subscribe((params) => {
      const sprintCode = params.get('id');

      if (sprintCode) {
        this.sprintService.getSprintById(sprintCode).subscribe({
          next: (res: SprintData) => {
            this.selectedSprint = res;
            this.cdr.detectChanges(); // Wake up change detection
          },
          error: (err: any) => {
            console.error('Error fetching specific sprint:', err);
          },
        });
      } else {
        this.selectedSprint = null;
        this.cdr.detectChanges();
      }
    });
  }

  loadSprints(): void {
    this.sprintService.getSprints().subscribe({
      next: (res: SprintData[]) => {
        this.sprints = res;
        this.cdr.detectChanges(); // Crucial: forces view to display records instantly
      },
      error: (err: any) => {
        console.error('Error loading sprints:', err);
      },
    });
  }

  loadTasks(): void {
    this.taskService.getTasks().subscribe({
      next: (res: any[]) => {
        this.allTasks = res;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Error loading tasks:', err);
      },
    });
  }

  getTasksForSprint(sprintId: string | undefined) {
    if (!sprintId) return [];
    return this.allTasks.filter((t: any) => t.sprintCode === 'SP' + sprintId);
  }

  getTaskCount(sprintId: string | undefined) {
    return this.getTasksForSprint(sprintId).length;
  }

  get totalTasks() {
    return this.allTasks.length;
  }

  get completedSprints() {
    return this.sprints.filter((s: SprintData) => s.status === 'Completed').length;
  }

  getSprintProgress(sprintId: string | undefined) {
    const tasks = this.getTasksForSprint(sprintId);
    if (!tasks || tasks.length === 0) return 0;

    const done = tasks.filter((t: any) => t.taskstatus === 'DONE' || t.taskstatus === 'CLOSED').length;
    return Math.round((done / tasks.length) * 100);
  }

  openSprintForm() {
    this.showSprintModal = true;
  }

  closeSprintForm() {
    this.showSprintModal = false;
  }

  calculateDuration(): void {
    if (!this.newSprint.startDate || !this.newSprint.endDate) {
      this.newSprint.sprintDuration = 0;
      return;
    }

    const start = new Date(this.newSprint.startDate);
    const end = new Date(this.newSprint.endDate);

    const diffTime = end.getTime() - start.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

    this.newSprint.sprintDuration = diffDays;
  }

  onDateChange() {
    if (!this.newSprint.startDate || !this.newSprint.endDate) return;

    if (!this.newSprint.sprintDuration) {
      this.calculateDuration();
    }
  }

  onDurationChange() {
    if (!this.newSprint.startDate || !this.newSprint.sprintDuration) return;

    const start = new Date(this.newSprint.startDate);
    const duration = this.newSprint.sprintDuration;

    const end = new Date(start);
    end.setDate(start.getDate() + duration);

    this.newSprint.endDate = end.toISOString().split('T')[0];
  }

  createSprint() {
    if (!this.newSprint.sprintName) return;

    this.sprintService.addSprint(this.newSprint).subscribe({
      next: () => {
        // All state updates happen inside 'next' after server responds successfully
        this.loadSprints();

        this.newSprint = {
          sprintName: '',
          description: '',
          productCode: null,
          startDate: '',
          endDate: '',
          sprintDuration: 0,
          status: 'Planned',
        };

        this.closeSprintForm(); // Safe choice inside the stream
      },
      error: (err: any) => {
        console.error('Error creating sprint:', err);
      },
    });
  }
  deleteSprint(id: string | undefined): void {
  if (!id) {
    console.error('Cannot delete sprint: Missing ID');
    return;
  }

  // A quick confirmation alert prevents accidental clicking disasters!
  if (confirm('Are you sure you want to delete this sprint?')) {
    this.sprintService.deleteSprint(id).subscribe({
      next: () => {
        // Refresh the list immediately so the deleted item vanishes from the screen
        this.loadSprints();

        // If the user happens to have this specific sprint open for viewing, deselect it
        if (this.selectedSprint?.id === id) {
          this.selectedSprint = null;
        }

        this.cdr.detectChanges(); // Tell Angular to redraw the screen
      },
      error: (err: any) => {
        console.error('Error deleting sprint:', err);
      }
    });
  }
}
}
