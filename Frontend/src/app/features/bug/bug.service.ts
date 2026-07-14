import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BugService {

  private apiUrl = 'http://localhost:8080/Bug';

  constructor(private http: HttpClient) {}

  private getAuthHeaders() {
    const token = localStorage.getItem('jwtToken');

    return {
      Authorization: `Bearer ${token}`
    };
  }

  getAllBugs(): Observable<any> {
    return this.http.get(this.apiUrl, {
      headers: this.getAuthHeaders()
    });
  }

  createBug(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/add`, data, {
      headers: this.getAuthHeaders()
    });
  }

  deleteBug(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  updateBug(id: number, data: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, data, {
      headers: this.getAuthHeaders()
    });
  }
}
