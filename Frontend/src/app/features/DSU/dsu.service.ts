import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DsuService {

  private baseUrl = 'http://localhost:8080/DSU';

  constructor(private http: HttpClient) {}

createDSU(
  entityType: string,
  entityCode: string,
  payload: any
): Observable<any> {

  const token = localStorage.getItem('jwtToken');

  return this.http.post(
    `${this.baseUrl}/${entityType}/${entityCode}`,
    payload,
    {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }
  );
}

getDsuById(id: string) {
  return this.http.get(`${this.baseUrl}/${id}`);
}

getReport(date: string): Observable<any> {

  const token = localStorage.getItem('jwtToken');

  return this.http.get(
    `${this.baseUrl}/date/${date}`,
    {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }
  );
}

generateAutoDSU(
  startDate: string,
  endDate: string
): Observable<any> {
  const token = localStorage.getItem('jwtToken');

  const headers = {
    Authorization: `Bearer ${token}`
  };

  return this.http.get(
    `${this.baseUrl}/auto?startDate=${startDate}&endDate=${endDate}`,
    { headers }
  );
}

}
