import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'http://localhost:8001/user';

  constructor(private http: HttpClient) {}

  login(email: string, password: string) {
  return this.http.post<{ access_token: string; role?: string }>(
    'YOUR_API_URL/login',
    { email, password }
  );
}

  saveToken(token: string) {
    localStorage.setItem('token', token);
  }

  getToken() {
    return localStorage.getItem('token');
  }

  logout() {
    localStorage.removeItem('token');
  }
}
