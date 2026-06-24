import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth/auth';
import { ApiService } from '../../core/apiService/api-service';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginResponse } from '../../dtos/LoginResponse';
import { LoginRequest } from '../../dtos/LoginRequest';

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
  errorMessage = signal('');

  constructor(
    private authService: AuthService,
    private router: Router,
    private apiService: ApiService,
  ) {}

  sendLoginRequest() {
    this.errorMessage.set(''); // Reset every login
    const payload: LoginRequest = { username: this.email, password: this.password };
    this.apiService.postRequest<LoginResponse>('/login', payload).subscribe({
      next: (response) => {
        const jwtToken: string = response.token;
        this.authService.saveToken(jwtToken);
        this.router.navigate(['/dashboard']);
        console.log('Server Response : ', response);
      },
      error: (err: HttpErrorResponse) => {
        switch (err.status) {
          case 401:
            this.errorMessage.set('Invalid username or password');
            break;
          case 500:
            this.errorMessage.set('Server error, try again later');
            break;
          default:
            this.errorMessage.set(err.error?.errorMessage || 'Login failed');
            break;
        }
      },
    });
  }

  onLogin() {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
      return;
    } else {
      if (localStorage.getItem('jwtToken') != null) {
        this.authService.logout();
      }
    }
    if (!this.email && !this.password) {
      this.errorMessage.set('Please enter email and password');
      return;
    } else if (!this.email) {
      this.errorMessage.set('Please enter email');
      return;
    } else if (!this.password) {
      this.errorMessage.set('Please enter password');
      return;
    }
    this.sendLoginRequest();
  }
}
