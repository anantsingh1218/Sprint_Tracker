import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TeamDashboardResponse } from '../features/dashboard/models/team-dashboard.model';
import { ApiService } from '../core/apiService/api-service';
import { Task } from '../features/dashboard/models/task.model';
import { Story } from '../features/dashboard/models/story.model';
import { Worklog } from '../features/dashboard/models/worklog.model';
import {
  DashboardResponse,
  ProductDropdown,
  Burndown,
  TeamCapacity,
  ReleaseReadiness,
  Velocity
} from '../features/dashboard/models/dashboard.model';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(
    private api: ApiService
  ) {}

  getDashboard(): Observable<DashboardResponse> {

    return this.api.getRequest<DashboardResponse>(
      '/dashboard'
    );

  }

  getTeamDashboard() {
    return this.api.getRequest<TeamDashboardResponse>('/dashboard');
}

  getProducts(): Observable<ProductDropdown[]> {

    return this.api.getRequest<ProductDropdown[]>(
      '/dashboard/products'
    );

  }

  getVelocity(productId: number): Observable<Velocity> {

    return this.api.getRequest<Velocity>(
      '/dashboard/velocity',
      {
        productId
      }
    );

  }

  getBurndown(productId: number): Observable<Burndown> {

    return this.api.getRequest<Burndown>(
      '/dashboard/burndown',
      {
        productId
      }
    );

  }

  getTeamCapacity(productId: number): Observable<TeamCapacity> {

    return this.api.getRequest<TeamCapacity>(
      '/dashboard/team-capacity',
      {
        productId
      }
    );

  }

  getReleaseReadiness(productId: number): Observable<ReleaseReadiness> {

    return this.api.getRequest<ReleaseReadiness>(
      '/dashboard/release',
      {
        productId
      }
    );

  }

  getSummary(productId: number) {

    return this.api.getRequest(
      '/dashboard/summary',
      {
        productId
      }
    );

  }

  exportDashboard(productId: number) {

    return this.api.getRequest(
      '/dashboard/export',
      {
        productId
      }
    );

  }

  getMyTasks() {

    return this.api.getRequest<Task[]>(
        '/dashboard/tasks'
    );

}

getMyStories() {

    return this.api.getRequest<any>(
        '/dashboard/stories'
    );

}

getFocusTask() {

    return this.api.getRequest<Task>(
        '/dashboard/focus-task'
    );

}

getRecentWorklogs() {

    return this.api.getRequest<Worklog[]>(
        '/dashboard/recent-worklogs'
    );

}
}