import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SprintService {

  private baseUrl = 'http://localhost:8080/sprint';

  constructor(private http: HttpClient) {}

  private getHeaders() {
    const token = localStorage.getItem('jwtToken');

    return {
      Authorization: `Bearer ${token}`
    };
  }

  getSprints(): Observable<any[]> {
    return this.http.get<any[]>(
      this.baseUrl,
      {
        headers: this.getHeaders()
      }
    );
  }

  getSprintById(id: number): Observable<any> {
    return this.http.get<any>(
      `${this.baseUrl}/${id}`,
      {
        headers: this.getHeaders()
      }
    );
  }

  addSprint(sprint: any): Observable<any> {
    return this.http.post(
      `${this.baseUrl}/add`,
      sprint,
      {
        headers: this.getHeaders()
      }
    );
  }

  updateSprint(id: number, sprint: any): Observable<any> {
    return this.http.put(
      `${this.baseUrl}/${id}`,
      sprint,
      {
        headers: this.getHeaders()
      }
    );
  }

  deleteSprint(id: number): Observable<any> {
    return this.http.delete(
      `${this.baseUrl}/${id}`,
      {
        headers: this.getHeaders()
      }
    );
  }
}
