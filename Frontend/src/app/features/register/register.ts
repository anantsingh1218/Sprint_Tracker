import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { ApiService } from '../../core/apiService/api-service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  username = '';
  password = '';
  email = '';
  role = 'DEVELOPER';

  loading = false;
  errorMessage = signal('');
  successMessage = signal('');

  constructor(
    private apiService: ApiService,
    private router: Router,
  ) {}

  register() {
    this.errorMessage.set('');
    this.successMessage.set('');

    if (!this.username.trim()) {
      this.errorMessage.set('Username is required');
      return;
    }

    if (!this.email.trim()) {
      this.errorMessage.set('Email is required');
      return;
    }

    if (!this.password.trim()) {
      this.errorMessage.set('Password is required');
      return;
    }

    if (!this.role.trim()) {
      this.errorMessage.set('Role is required');
      return;
    }

    const payload = {
      username: this.username,
      email: this.email,
      password: this.password,
      role: this.role,
    };

    this.apiService.postRequest('/register', payload).subscribe({
      next: () => {
        this.successMessage.set('User registered successfully');

        this.username = '';
        this.password = '';
        this.email = '';
        this.role = 'ROLE_Developer';
      },

      error: (err: HttpErrorResponse) => {
        switch (err.status) {
          case 409:
            this.errorMessage.set('User already exists');
            break;

          case 400: {
            const errors: string[] = [];

            if (err.error.email) {
              errors.push(err.error.email);
            }

            if (err.error.password) {
              errors.push(err.error.password);
            }

            if (err.error.userName) {
              errors.push(err.error.userName);
            }
            if (err.error.roles) {
              errors.push(err.error.roles);
            }

            this.errorMessage.set(errors.length > 0 ? errors.join('\n') : 'Invalid data');

            break;
          }

          default:
            this.errorMessage.set(err.error?.message || 'Registration failed');
        }
      },
    });
  }
  goToDashboard(){
    this.router.navigate(['/dashboard']);
  }
}
