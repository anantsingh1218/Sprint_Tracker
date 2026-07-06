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

  // GET all sprints
  getSprints(): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.baseUrl}/all`,
      {
        headers: this.getHeaders()
      }
    );
  }

  // GET sprint by ID
  getSprintById(id: number): Observable<any> {
    return this.http.get<any>(
      `${this.baseUrl}/${id}`,
      {
        headers: this.getHeaders()
      }
    );
  }

  // ADD sprint
  addSprint(sprint: any): Observable<any> {
    return this.http.post<any>(
      `${this.baseUrl}/add`,
      sprint,
      {
        headers: this.getHeaders()
      }
    );
  }

  // UPDATE sprint
  updateSprint(id: number, sprint: any): Observable<any> {
    return this.http.put<any>(
      `${this.baseUrl}/${id}`,
      sprint,
      {
        headers: this.getHeaders()
      }
    );
  }

  // DELETE sprint
  deleteSprint(id: number): Observable<any> {
    return this.http.delete<any>(
      `${this.baseUrl}/${id}`,
      {
        headers: this.getHeaders()
      }
    );
  }
}
