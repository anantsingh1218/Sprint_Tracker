import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

import { SprintService } from './sprint.service';
import { TaskService } from '../tasks/task.service';

@Component({
  selector: 'app-sprint',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './sprint.html',
  styleUrl: './sprint.css',
})
export class Sprint implements OnInit {

  sprints: any[] = [];
  selectedSprint: any = null;
  showSprintModal = false;

  newSprint = {
    sprintName: '',
    description: '',
    productId: null,
    startDate: '',
    endDate: '',
    sprintDuration: 0,
    status: 'Planned',
  };

  constructor(
    private sprintService: SprintService,
    private taskService: TaskService,
    private route: ActivatedRoute
  ) {}


  ngOnInit(): void {
  this.loadSprints();

  this.route.paramMap.subscribe(params => {
    const sprintId = params.get('id');

    if (sprintId) {
      this.sprintService.getSprintById(Number(sprintId)).subscribe({
        next: (res: any) => {
          this.selectedSprint = res;
        },
        error: (err: any) => {
          console.error(err);
        }
      });
    } else {
      this.selectedSprint = null;
    }
  });
}

  loadSprints(): void {
    this.sprintService.getSprints().subscribe({
      next: (res: any[]) => {
        this.sprints = res;
      },
      error: (err: any) => {
        console.error('Error loading sprints:', err);
      }
    });
  }

  getTasksForSprint(sprintId: string) {
    return this.taskService.getTasksBySprint(sprintId) || [];
  }

  getTaskCount(sprintId: string) {
    return this.taskService.getTasksBySprint(sprintId).length;
  }

  get totalTasks() {
    return this.taskService.getTasks().length;
  }

  get completedSprints() {
    return this.sprints.filter((s: any) => s.status === 'Completed').length;
  }

  getSprintProgress(sprintId: string) {
    const tasks = this.taskService.getTasksBySprint(sprintId);

    if (!tasks || tasks.length === 0) return 0;

    const done = tasks.filter((t: any) => t.status === 'Done').length;

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
      const start = new Date(this.newSprint.startDate);
      const end = new Date(this.newSprint.endDate);

      const diffTime = end.getTime() - start.getTime();
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

      this.newSprint.sprintDuration = diffDays;
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
    console.log(this.newSprint);
    this.sprintService.addSprint(this.newSprint).subscribe({
      next: () => {
        this.loadSprints();

        this.newSprint = {
          sprintName: '',
          description: '',
          productId:null,
          startDate: '',
          endDate: '',
          sprintDuration: 0,
          status: 'Planned',
        };

        this.showSprintModal = false;
      },
      error: (err: any) => {
        console.error('Error creating sprint:', err);
      }
    });
    this.closeSprintForm()
  }
}
