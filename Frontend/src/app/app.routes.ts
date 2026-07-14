import { Routes } from '@angular/router';
import { LoginComponent } from './features/login/login';
import { Dashboard } from './features/dashboard/dashboard';
import { Layout } from './core/layout/layout';
import { Sprint } from './features/sprint/sprint';
import { Tasks } from './features/tasks/tasks';
import { Backlog } from './features/backlog/backlog';
import { Board } from './features/board/board';
import { StoryList } from './features/story-list/story-list';
import { Register } from './features/register/register';
import { pmGuard } from './core/guards/pm-guard';
import { DsuService } from './features/DSU/dsu.service';
import { ForgotPassword } from './features/forgot-password/forgot-password';
import { FeatureList } from './features/feature-list/feature-list';
import { TeamDashboard } from './features/dashboard/TeamDashboard/teamDashboard';

import { BugList } from './features/bug-list/bug-list';
import { Profile } from './features/profile/profile';
import { DsuComponent } from './features/DSU/dsu';
import { IntegrateView } from './features/integrated-view/integrated-view';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // Auth routes (NO layout)
  { path: 'login', component: LoginComponent },
  { path: 'forgot-password', component: ForgotPassword },
  {
    path: 'register',
    component: Register,
    canActivate: [pmGuard],
  },

  // APP routes (WITH layout)
  {
    path: '',
    component: Layout,
    children: [
      { path: 'dashboard', component: Dashboard },
      { path: 'teamDashboard', component: TeamDashboard },
      { path: 'sprints', component: Sprint },
      { path: 'tasks', component: Tasks },
      { path: 'tasks/:id', component: Tasks },
      { path: 'backlog', component: Backlog },
      { path: 'integrated-view', component: IntegrateView },
      { path: 'board', component: Board },
      { path: 'story', component: StoryList },
      { path: 'dsu', component: DsuComponent },
      { path: 'feature', component: FeatureList },
      { path: 'bugs', component: BugList },
      { path: 'profile', component: Profile },
      {
        path: 'sprints/:id',
        component: Sprint,
      },
    ],
  },

  // fallback
  { path: '**', redirectTo: 'login' },
];
