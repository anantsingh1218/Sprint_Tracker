import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Task } from '../models/task.model';
import { DashboardService } from '../../../services/dashboardService';
import { Story } from '../models/story.model';
import { Worklog } from '../models/worklog.model';
import {
  TeamDashboardResponse,
  TeamDashboardData
} from '../models/team-dashboard.model';

@Component({
  selector: 'app-team-dashboard',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './teamDashboard.html',
  styleUrl: './teamDashboard.css'
})
export class TeamDashboard implements OnInit {

  loading = true;

  teamDashboard!: TeamDashboardResponse;

  tasks: Task[] = [];

  stories: Story[] = [];

  focusTask!: Task;

  recentWorklogs: Worklog[] = [];

  constructor(
    private dashboardService: DashboardService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.fetchDashboard();

    this.fetchMyTasks();

    this.fetchMyStories();

    this.fetchFocusTask();

    this.fetchRecentWorklogs();

    this.cdr.detectChanges();
  }

  fetchDashboard(): void {

    this.dashboardService
    .getTeamDashboard()
    .subscribe({
        next: (res) => {
            this.teamDashboard = res;
            this.loading = false;
        },
        error: (err) => {
            console.error(err);
            this.loading = false;
        }
    });

  }

  get assignedTasks(): number {

    return this.teamDashboard?.dashboard?.assignedTasks ?? 0;

  }

  get completedTasks(): number {

    return this.teamDashboard?.dashboard?.completedTasks ?? 0;

  }

  get pendingTasks(): number {

    return this.teamDashboard?.dashboard?.pendingTasks ?? 0;

  }

  fetchMyTasks(): void {

    this.dashboardService
        .getMyTasks()
        .subscribe({

            next: (res: any) => {

    console.log(res);

    this.tasks = res.content;

},

            error: (err) => {

                console.error(err);

            }

        });

}

fetchMyStories(): void {

    this.dashboardService
        .getMyStories()
        .subscribe({

            next: (res: any) => {

                console.log("Stories", res);

                this.stories = res.content;

            },

            error: err => {

                console.error(err);

            }

        });

}

fetchFocusTask(): void {

    this.dashboardService
        .getFocusTask()
        .subscribe({

            next: (res) => {

                this.focusTask = res;

            },

            error: (err) => {

                console.error(err);

            }

        });

}

fetchRecentWorklogs(): void {

    this.dashboardService

        .getRecentWorklogs()

        .subscribe({

            next: (res) => {

                console.log("Recent Worklogs", res);

                this.recentWorklogs = res;

            },

            error: err => {

                console.error(err);

            }

        });

}

}