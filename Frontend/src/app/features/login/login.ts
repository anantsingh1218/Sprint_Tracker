import { Component, OnDestroy, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth/auth';
import { ApiService } from '../../core/apiService/api-service';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginResponse } from '../../dtos/LoginResponse';
import { LoginRequest } from '../../dtos/LoginRequest';
import { SystemService } from '../../services/systemService';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class LoginComponent implements OnInit, OnDestroy {
  email = '';
  password = '';
  loading = false;
  errorMessage = signal('');
  isInitialized: boolean = false;
  private destroy$ = new Subject<void>();

  constructor(
    private authService: AuthService,
    private router: Router,
    private apiService: ApiService,
    private systemService: SystemService,
  ) {}

  ngOnInit(): void {
    this.systemService.loadStatus();

    this.systemService.status$.pipe(takeUntil(this.destroy$)).subscribe((system) => {
      this.isInitialized = system?.initialized ?? false;
    });
  }
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

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
    this.authService.logout();
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
