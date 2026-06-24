import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth/auth';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar implements OnInit {

  username: string | null = null;
  role: string | null = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.role = this.authService.getRole();
    this.username = this.authService.getUsername();
  }

  get isProjectManager(): boolean {
    return this.role?.endsWith('PM') ?? false;
  }

  navigateToRegister(): void {
    this.router.navigate(['/register']);
  }
}