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

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // Auth routes (NO layout)
  { path: 'login', component: LoginComponent },
  {
    path: 'register',
    component: Register,
    canActivate: [pmGuard]
  },
  

  // APP routes (WITH layout)
  {
    path: '',
    component: Layout,
    children: [
      { path: 'dashboard', component: Dashboard },
      { path: 'sprints', component: Sprint },
      { path: 'tasks', component: Tasks },
      { path: 'backlog', component: Backlog },
      { path: 'board', component: Board },
      { path: 'story', component: StoryList },
    ],
  },

  // fallback
  { path: '**', redirectTo: 'login' },
];
