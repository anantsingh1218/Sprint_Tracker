import { Injectable } from '@angular/core';
import { ApiService } from '../../core/apiService/api-service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(private apiService: ApiService) {}

  getTasks(): Observable<any[]> {
    return this.apiService.getRequest<any[]>('/task/all');
  }

  addTask(task: any): Observable<any> {
    return this.apiService.postRequest<any>('/task/add', task);
  }

  getTaskById(taskCode: string): Observable<any> {
    return this.apiService.getRequest<any>(`/task/${taskCode}`);
  }

  getTasksBySprint(sprintCode: string): Observable<any[]> {
    return this.apiService.getRequest<any[]>(`/task/sprint/${sprintCode}`);
  }

  updateTaskStatus(task: any, status: string): Observable<any> {
    const updatedTask = { ...task, status: status };
    return this.apiService.putRequest<any>(`/task/${task.taskCode}`, updatedTask);
  }

  updateTask(updatedTask: any): Observable<any> {
    return this.apiService.putRequest<any>(`/task/${updatedTask.taskCode}`, updatedTask);
  }

  getUsersDropdown(): Observable<any[]> {
    return this.apiService.getRequest<any[]>('/users/all');
  }

  getSprintsDropdown(): Observable<any[]> {
    return this.apiService.getRequest<any[]>('/sprint/all');
  }

  getStoriesDropdown(): Observable<any[]> {
    return this.apiService.getRequest<any[]>('/story/all');
  }
}
