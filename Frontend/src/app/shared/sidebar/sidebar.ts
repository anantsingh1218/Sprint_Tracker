import { Component, EventEmitter, Input, Output } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { jwtDecode } from 'jwt-decode';

interface JwtPayload {

  username: string;

  roles: string;

}

interface MenuItem {

  label: string;

  route: string;

  roles: string[];

}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css'
})
export class Sidebar {

  @Input()
  collapsed = false;

  @Output()
  toggle = new EventEmitter<void>();

  role = '';

  menuItems: MenuItem[] = [

    {
      label: 'Dashboard',
      route: '/dashboard',
      roles: ['ROLE_PM','ROLE_Scrum_Master',  'ROLE_Developer', 'ROLE_QA', 'ROLE_BA']
    },

    {
      label: 'Board',
      route: '/board',
      roles: ['ROLE_PM', 'ROLE_Scrum_Master', 'ROLE_Developer', 'ROLE_QA', 'ROLE_BA']
    },

    {
      label: 'Features',
      route: '/feature',
      roles: ['ROLE_PM', 'ROLE_Scrum_Master']
    },

    {
      label: 'Stories',
      route: '/story',
      roles: ['ROLE_PM','ROLE_Scrum_Master']
    },

    {
      label: 'Tasks',
      route: '/tasks',
      roles: ['ROLE_PM', 'ROLE_Scrum_Master']
    },

    {
      label: 'Bugs',
      route: '/bugs',
      roles: ['ROLE_PM', 'ROLE_Scrum_Master', 'ROLE_Developer', 'ROLE_QA', 'ROLE_BA']
    },

    {
      label: 'Sprints',
      route: '/sprints',
      roles: ['ROLE_PM', 'ROLE_Scrum_Master']
    },

    {
      label: 'Backlog',
      route: '/backlog',
      roles: ['ROLE_PM', 'ROLE_Scrum_Master']
    },

    {
      label: 'Integrated View',
      route: '/integrated-view',
      roles: ['ROLE_PM','ROLE_Scrum_Master', 'ROLE_Developer', 'ROLE_QA', 'ROLE_BA']
    },

    {
      label: 'DSU',
      route: '/dsu',
      roles: ['ROLE_PM', 'ROLE_Scrum_Master']
    }

  ];

  constructor() {

    const token = localStorage.getItem('jwtToken');

    if (token) {

      const decoded = jwtDecode<JwtPayload>(token);

      this.role = decoded.roles;

      console.log('Logged Role:', this.role);

    }

  }

  get visibleMenu(): MenuItem[] {

    return this.menuItems.filter(

      item => item.roles.includes(this.role)

    );

  }

  toggleSidebar() {

    this.toggle.emit();

  }

}