import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TeamDashboardResponse } from '../features/dashboard/models/team-dashboard.model';
import { ApiService } from '../core/apiService/api-service';
import { Task } from '../features/dashboard/models/task.model';
import { Story } from '../features/dashboard/models/story.model';
import { Worklog } from '../features/dashboard/models/worklog.model';
import { BoardColumn } from '../features/board/models/board.model';
import { MoveTask } from '../features/board/models/move-task.model';
import { SprintDropdown } from '../features/board/models/sprint-dropdown.model';
import { ExportDashboard } from '../features/dashboard/models/export-dashboard.model';
import { SprintProgress } from '../features/dashboard/models/dashboard.model';
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

  getSprintProgress(productId:number){

    return this.api.getRequest<SprintProgress>(

        '/dashboard/sprint-progress',

        { productId }

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

    return this.api.getRequest<ExportDashboard>(
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

getBoard(sprintId: number) {

  return this.api.getRequest<BoardColumn[]>(

    `/dashboard/board/${sprintId}`

  );

}

moveTask(data: MoveTask) {

    return this.api.patchRequest(

        '/dashboard/task/move',

        data

    );

}

getSprints() {

    return this.api.getRequest<SprintDropdown[]>(

        '/dashboard/sprints'

    );

}
}