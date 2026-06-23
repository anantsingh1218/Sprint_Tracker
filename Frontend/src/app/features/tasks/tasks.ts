import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { SprintService } from '../../core/sprint/sprint';
import { TaskService } from '../../core/task/task';

import {
  CdkDragDrop,
  DragDropModule
} from '@angular/cdk/drag-drop';

import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-tasks',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    DragDropModule,
    MatAutocompleteModule,
    MatInputModule
  ],
  templateUrl: './tasks.html',
  styleUrl: './tasks.css',
})
export class Tasks {

  showTaskModal = false;
  showEditModal = false;

  selectedSprintId = '';

  users = ['User 1', 'User 2', 'User 3', 'User 4', 'User 5'];

  newTask = {
    title: '',
    description: '',
    priority: 'Medium',
    status: 'Open',
    assignedTo: '',
    originalEstimate: '',
    remainingEstimate: ''
  };

  editTask: any = null;

  constructor(
    private sprintService: SprintService,
    private taskService: TaskService
  ) {}

  get sprints() {
    return this.sprintService.getSprints();
  }

  get allTasks() {
    return this.taskService.getTasks();
  }

  get filteredUsers() {
    const search =
      (this.editTask?.assignedTo || this.newTask.assignedTo || '').toLowerCase();

    return this.users.filter(user =>
      user.toLowerCase().includes(search)
    );
  }

  get filteredTasks() {
    if (!this.selectedSprintId) return this.allTasks;

    return this.allTasks.filter(
      t => t.sprintId === this.selectedSprintId
    );
  }

  get todoTasks() {
    return this.filteredTasks.filter(t => t.status === 'Open');
  }

  get inProgressTasks() {
    return this.filteredTasks.filter(t => t.status === 'In Progress');
  }

  get doneTasks() {
    return this.filteredTasks.filter(
      t => t.status === 'Done' || t.status === 'Closed'
    );
  }

  openTaskForm() {
    this.showTaskModal = true;
  }

  closeTaskForm() {
    this.showTaskModal = false;
  }

  openEditTask(task: any) {
    this.editTask = { ...task };
    this.showEditModal = true;
  }

  closeEditTask() {
    this.showEditModal = false;
    this.editTask = null;
  }

  createTask() {
    if (!this.newTask.title) return;

    const original = Number(this.newTask.originalEstimate);
    const remaining = Number(this.newTask.remainingEstimate);

    if (original < 0 || remaining < 0) {
      alert('Estimates cannot be negative');
      return;
    }

    if (remaining > original) {
      alert('Remaining cannot be greater than original');
      return;
    }

    this.taskService.addTask({
      ...this.newTask,
      sprintId: this.selectedSprintId
    });

    this.newTask = {
      title: '',
      description: '',
      priority: 'Medium',
      status: 'Open',
      assignedTo: '',
      originalEstimate: '',
      remainingEstimate: ''
    };

    this.closeTaskForm();
  }

  saveTaskChanges() {
    const original = Number(this.editTask.originalEstimate);
    const remaining = Number(this.editTask.remainingEstimate);

    if (original < 0 || remaining < 0) {
      alert('Estimates cannot be negative');
      return;
    }

    if (remaining > original) {
      alert('Remaining cannot be greater than original');
      return;
    }

    this.taskService.updateTask(this.editTask);
    this.closeEditTask();
  }

  drop(event: CdkDragDrop<any[]>, newStatus: string) {
    const task =
      event.previousContainer.data[event.previousIndex];

    this.taskService.updateTaskStatus(task, newStatus);
  }

  trackById(index: number, item: any) {
    return item.id;
  }
}
