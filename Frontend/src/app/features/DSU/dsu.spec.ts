import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DsuService {

  private baseUrl = 'http://localhost:8080/DSU';

  constructor(private http: HttpClient) {}

  createDSU(entityType: string, entityId: number, payload: any): Observable<any> {
    return this.http.post(
      `${this.baseUrl}/${entityType}/${entityId}`,
      payload
    );
  }

  getReport(date: string): Observable<any> {
    return this.http.get(
      `${this.baseUrl}/date/${date}`
    );
  }
}
