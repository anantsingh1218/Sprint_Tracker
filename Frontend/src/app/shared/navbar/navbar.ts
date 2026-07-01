import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth/auth';
import { UpperCasePipe } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [UpperCasePipe],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar implements OnInit {

  username: string | null = null;
  role: string | null = null;

  isMenuOpen: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.username = this.authService.getUsername();
    this.role = this.authService.getRole();
  }

  // Check if logged-in user is Project Manager
  get isProjectManager(): boolean {
    return this.role?.endsWith('PM') ?? false;
  }

  // Navigate to register page
  navigateToRegister(): void {
    this.router.navigate(['/register']);
  }

  // Toggle profile menu
  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  // Navigate to profile page
  goToProfile(): void {
    if (this.router.url === '/profile') {
      window.location.reload();
    } else {
      this.router.navigate(['/profile']);
    }
    this.isMenuOpen = false;
  }

  // Logout user
  logout(): void {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('role');
    localStorage.removeItem('username');

    this.router.navigate(['/login']);
  }
}
