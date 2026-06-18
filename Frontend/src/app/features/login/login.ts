import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class LoginComponent {
  email = '';
  password = '';
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onLogin() {
    if (!this.email || !this.password) {
      alert('Please enter email and password');
      return;
    }

    this.loading = true;

    this.authService.login(this.email, this.password).subscribe({
      next: (res: { access_token: string; role?: string }) => {
        this.loading = false;

        console.log('Login successful:', res);

        localStorage.setItem('token', res.access_token);

        if (res.role) {
          localStorage.setItem('role', res.role);
        }

        alert('Login successful!');

        this.router.navigate(['/dashboard']);
      },

      error: (err) => {
        this.loading = false;

        console.error('Login failed:', err);

        alert(
          err?.error?.message ||
          err?.error?.detail ||
          'Invalid email or password'
        );
      },
    });
  }
}
