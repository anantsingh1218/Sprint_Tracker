import { Component, OnDestroy, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { ApiService } from '../../core/apiService/api-service';
import { firstValueFrom, Subject, takeUntil } from 'rxjs';
import { SystemService } from '../../services/systemService';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register implements OnInit, OnDestroy {
  username = '';
  password = '';
  email = '';
  role = 'DEVELOPER';
  isInitialized: boolean = false;
  loading = false;
  errorMessage = signal('');
  successMessage = signal('');
  private destroy$ = new Subject<void>();

  roles = [
    { value: 'ROLE_Developer', label: 'Developer' },
    { value: 'ROLE_BA', label: 'Business Analyst' },
    { value: 'ROLE_PM', label: 'Project Manager' },
    { value: 'ROLE_QA', label: 'Quality Assurance' },
    { value: 'ROLE_Scrum_Master', label: 'Scrum Master' },
  ];

  get visibleRoles() {
    return this.isInitialized ? this.roles : this.roles.filter((r) => r.value === 'ROLE_PM');
  }

  constructor(
    private systemService: SystemService,
    private apiService: ApiService,
    private router: Router,
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

    const backendApiLink = this.isInitialized ? '/register' : '/bootstrap-admin';

    this.apiService.postRequest(backendApiLink, payload).subscribe({
      next: (response) => {
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
  goToDashboard() {
    this.router.navigate(['/dashboard']);
  }
  goToLogin() {
    this.router.navigate(['/login']);
  }
}
