import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'http://localhost:8080/login';

  constructor(private http: HttpClient) {}

  login(username: string, password: string) {
  return this.http.post<{ access_token: string; role?: string }>(
    'YOUR_API_URL/login',
    { username, password }
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
