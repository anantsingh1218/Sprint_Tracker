import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { SprintService } from '../../core/sprint/sprint';
import { TaskService } from '../../core/task/task';

import {
  CdkDragDrop,
  DragDropModule
} from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-tasks',
  standalone: true,
  imports: [CommonModule, FormsModule, DragDropModule],
  templateUrl: './tasks.html',
  styleUrl: './tasks.css',
})
export class Tasks {

  // UI state
  showTaskModal = false;

  // filters
  selectedSprintId = '';

  // form model
  newTask = {
    title: '',
    description: '',
    priority: 'Medium'
  };

  constructor(
    private sprintService: SprintService,
    private taskService: TaskService
  ) {}

  // Sprint list
  get sprints() {
    return this.sprintService.getSprints();
  }

  // all tasks
  get allTasks() {
    return this.taskService.getTasks();
  }

  // filtered by sprint
  get filteredTasks() {
    if (!this.selectedSprintId) {
      return this.allTasks;
    }

    return this.allTasks.filter(
      t => t.sprintId === this.selectedSprintId
    );
  }

  // Kanban groups
  get todoTasks() {
    return this.filteredTasks.filter(t => t.status === 'To Do');
  }

  get inProgressTasks() {
    return this.filteredTasks.filter(t => t.status === 'In Progress');
  }

  get doneTasks() {
    return this.filteredTasks.filter(t => t.status === 'Done');
  }

  // Create task
  createTask() {
    if (!this.newTask.title) return;

    this.taskService.addTask({
      title: this.newTask.title,
      description: this.newTask.description,
      priority: this.newTask.priority,
      status: 'To Do',
      sprintId: this.selectedSprintId
    });

    this.newTask = {
      title: '',
      description: '',
      priority: 'Medium'
    };

    this.showTaskModal = false;
  }

  // Drag & drop (status update)
  drop(event: CdkDragDrop<any[]>, newStatus: string) {
    const task =
      event.previousContainer.data[event.previousIndex];

    this.taskService.updateTaskStatus(task, newStatus);
  }
}
