import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { ApiService } from '../../core/apiService/api-service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './forgot-password.html',
  styleUrl: './forgot-password.css',
})
export class ForgotPassword {

  email: string | null = null;
  username: string | null = null;

  errorMessage = signal('');
  successMessage = signal('');

  constructor(
    private apiService: ApiService,
    private router: Router
  ) {}

  forgotPassword() {
    this.errorMessage.set('');
    this.successMessage.set('');

    if (!this.email?.trim() || this.email == null) {
      this.errorMessage.set('Email is required');
      return;
    }
    if (!this.username?.trim() || this.username == null) {
      this.errorMessage.set('Username is required');
      return;
    }

    const payload = {
      username: this.username,
      email: this.email
    };

    this.apiService.patchRequest('/forgot-password', payload).subscribe({
      next: (res: any) => {
        this.successMessage.set(
          res.response || 'Temporary password has been generated successfully'
        );

        this.email = null;
        this.username = null;
      },

      error: (err: HttpErrorResponse) => {
        switch (err.status) {

          case 404:
            this.errorMessage.set('User not found');
            break;

          case 401:
            this.errorMessage.set('Invalid Credentials');
            break;

          default:
            this.errorMessage.set(err.error?.message || 'Something went wrong');
        }
      }
    });
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}