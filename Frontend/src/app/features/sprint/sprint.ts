import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { SprintService } from '../../core/sprint/sprint';
import { TaskService } from '../../core/task/task';

@Component({
  selector: 'app-sprint',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './sprint.html',
  styleUrl: './sprint.css'
})
export class Sprint {
  getTasksForSprint(sprintId: string) {
  return this.taskService.getTasksBySprint(sprintId);
}

getTaskCount(sprintId: string) {
  return this.taskService.getTasksBySprint(sprintId).length;
}

get totalTasks() {
  return this.taskService.getTasks().length;
}

get completedSprints() {
  return this.sprints.filter(
    (s: any) => s.status === 'Completed'
  ).length;
}

getSprintProgress(sprintId: string) {
  const total = this.getTaskCount(sprintId);

  if (total === 0) {
    return 0;
  }

  // Demo progress calculation
  return Math.min(total * 20, 100);
}

  showSprintModal = false;

  newSprint = {
    name: '',
    description: '',
    startDate: '',
    endDate: '',
    duration: '', // days
    status: 'Planned'
  };

  constructor(
  private sprintService: SprintService,
  private taskService: TaskService
) {}

  get sprints() {
    return this.sprintService.getSprints();
  }

  openSprintForm() {
    this.showSprintModal = true;
  }

  closeSprintForm() {
    this.showSprintModal = false;
  }

  calculateDuration(): void {
    if (!this.newSprint.startDate || !this.newSprint.endDate) {
      this.newSprint.duration = '0';
    }

    const start = new Date(this.newSprint.startDate);
    const end = new Date(this.newSprint.endDate);

    const diffTime = end.getTime() - start.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

    this.newSprint.duration = String(diffDays)
  }

  onDateChange() {
    if (!this.newSprint.startDate || !this.newSprint.endDate) {
      return;
    }

    // Only auto-calculate duration if user hasn't entered one
    if (!this.newSprint.duration) {
      const start = new Date(this.newSprint.startDate);
      const end = new Date(this.newSprint.endDate);

      const diffTime = end.getTime() - start.getTime();
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

      this.newSprint.duration = diffDays.toString();
    }
  }

  onDurationChange() {
    if (!this.newSprint.startDate || !this.newSprint.duration) {
      return;
    }

    const start = new Date(this.newSprint.startDate);
    const duration = Number(this.newSprint.duration);

    const end = new Date(start);
    end.setDate(start.getDate() + duration);

    this.newSprint.endDate = end.toISOString().split('T')[0];
  }

  createSprint() {
    if (!this.newSprint.name) return;

    this.sprintService.addSprint(this.newSprint);

    this.newSprint = {
  name: '',
  description: '',
  duration: '',
  startDate: '',
  endDate: '',
  status: 'Planned'
};

    this.showSprintModal = false;
  }
}
