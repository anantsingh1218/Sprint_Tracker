import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private tasks: any[] = [
  {
    id: '1',
    title: 'Gather Requirements',
    description: 'Collect lead requirements',
    status: 'Done',
    priority: 'Medium',
    sprintId: '1'
  },
  {
    id: '2',
    title: 'Design Lead Screen',
    description: 'Create UI for lead form',
    status: 'In Progress',
    priority: 'High',
    sprintId: '1'
  }
];

  getTasks() {
    return this.tasks;
  }

  addTask(task: any) {
    this.tasks.push({
      ...task,
      id: crypto.randomUUID() // IMPORTANT for edit feature
    });
  }

  getTaskById(id: string) {
  return this.tasks.find((t: any) => t.id === id);
}



  getTasksBySprint(sprintId: string) {
    return this.tasks.filter(t => t.sprintId === sprintId);
  }

  updateTaskStatus(task: any, status: string) {
    const found = this.tasks.find(t => t.id === task.id);
    if (found) {
      found.status = status;
    }
  }

  // ✅ NEW: required for Edit Task feature
  updateTask(updatedTask: any) {
    const index = this.tasks.findIndex(t => t.id === updatedTask.id);

    if (index !== -1) {
      this.tasks[index] = {
        ...this.tasks[index],
        ...updatedTask
      };
    }
  }
}
