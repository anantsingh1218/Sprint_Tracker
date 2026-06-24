import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { JwtPayload } from '../../shared/interfaces/jwtPayloadInterface';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private JWT_TOKEN_HEADER = 'jwtToken';

  saveToken(token: string) {
    localStorage.setItem(this.JWT_TOKEN_HEADER, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.JWT_TOKEN_HEADER);
  }

  private decodeToken(): JwtPayload | null {
    const token = this.getToken();
    if (!token) return null;

    return jwtDecode<JwtPayload>(token);
  }

  getUsername(): string | null {
    return this.decodeToken()?.username ?? null;
  }

  getRole(): string | null {
    return this.decodeToken()?.roles ?? null;
  }

  getExpiration(): Date | null {
    const exp = this.decodeToken()?.exp;
    return exp ? new Date(exp * 1000) : null;
  }

  isTokenExpired(): boolean {
    const exp = this.decodeToken()?.exp;
    if (!exp) return true;

    return Date.now() > exp * 1000;
  }

  isLoggedIn(): boolean {
    return !!this.getToken() && !this.isTokenExpired();
  }

  logout() {
    localStorage.removeItem(this.JWT_TOKEN_HEADER);
  }
}
