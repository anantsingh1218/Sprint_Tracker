import { Routes } from '@angular/router';
import { LoginComponent } from './features/login/login';
import { Dashboard } from './features/dashboard/dashboard';
import { Layout } from './core/layout/layout';
import { Sprint } from './features/sprint/sprint';
import { Tasks } from './features/tasks/tasks';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // Auth routes (NO layout)
  { path: 'login', component: LoginComponent },

  // APP routes (WITH layout)
  {
    path: '',
    component: Layout,
    children: [
      { path: 'dashboard', component: Dashboard },
      { path: 'sprints', component: Sprint },
      { path: 'tasks', component: Tasks },
    ]
  },

  // fallback
  { path: '**', redirectTo: 'login' }
];
