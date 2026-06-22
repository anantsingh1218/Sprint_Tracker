import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private tasks: any[] = [];

  getTasks() {
    return this.tasks;
  }

  addTask(task: any) {
    this.tasks.push(task);
  }

  getTasksBySprint(sprintId: string) {
    return this.tasks.filter(t => t.sprintId === sprintId);
  }

  updateTaskStatus(task: any, status: string) {
    const found = this.tasks.find(t => t === task);
    if (found) {
      found.status = status;
    }
  }
}
