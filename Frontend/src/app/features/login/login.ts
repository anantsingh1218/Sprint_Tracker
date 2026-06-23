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
  errorMessage: string = ''
  errorMessageUpdated: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

   onLogin() {
  if (!this.email && !this.password) {
    // TODO: Change to inside Login Component
    // TODO: Add username only or password only cases also.
    // alert('Please enter email and password');
    this.errorMessage = 'Please enter email and password'
    this.errorMessageUpdated = true
    return;
  }
  else if (!this.email){
    // alert('Please enter email and password');
    this.errorMessage = 'Please enter email'
    this.errorMessageUpdated = true
    return;
  }
  else if (!this.password){
    // alert('Please enter email and password');
    this.errorMessage = 'Please enter password'
    this.errorMessageUpdated = true
    return;
  }
  // Temporary frontend-only login

  localStorage.setItem('token', 'dummy-token');

  // TODO: Change to inside Login Component
  // alert('Login successful!');
  this.errorMessage = 'Login Successful'
  this.errorMessageUpdated = true

  this.router.navigate(['/dashboard']);
}}
