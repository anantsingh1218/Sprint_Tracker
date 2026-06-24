import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  private buildParams(
    requestParams?: Record<string, string | number | boolean | null | undefined>,
  ): HttpParams {
    let params = new HttpParams();

    if (!requestParams) return params;

    Object.keys(requestParams).forEach((key) => {
      const value = requestParams[key];
      if (value !== null && value !== undefined) {
        params = params.set(key, String(value));
      }
    });

    return params;
  }

  getRequest<T>(endpoint: string, requestParams?: Record<string, any>): Observable<T> {
    return this.http
      .get<T>(`${this.apiUrl}${endpoint}`, {
        params: this.buildParams(requestParams),
      })
      .pipe(catchError(this.apiErrorHandler));
  }

  postRequest<T>(
    endpoint: string,
    payload: any,
    requestParams?: Record<string, any>,
    contentType: string = 'application/json',
  ): Observable<T> {
    const headers = new HttpHeaders({
      'Content-Type': contentType,
    });

    return this.http
      .post<T>(`${this.apiUrl}${endpoint}`, payload, {
        headers,
        params: this.buildParams(requestParams),
      })
      .pipe(catchError(this.apiErrorHandler));
  }

  putRequest<T>(
    endpoint: string,
    payload: any,
    requestParams?: Record<string, any>,
  ): Observable<T> {
    return this.http
      .put<T>(`${this.apiUrl}${endpoint}`, payload, {
        params: this.buildParams(requestParams),
      })
      .pipe(catchError(this.apiErrorHandler));
  }

  patchRequest<T>(
    endpoint: string,
    payload: any,
    requestParams?: Record<string, any>,
  ): Observable<T> {
    return this.http
      .patch<T>(`${this.apiUrl}${endpoint}`, payload, {
        params: this.buildParams(requestParams),
      })
      .pipe(catchError(this.apiErrorHandler));
  }

  deleteRequest<T>(endpoint: string, requestParams?: Record<string, any>): Observable<T> {
    return this.http
      .delete<T>(`${this.apiUrl}${endpoint}`, {
        params: this.buildParams(requestParams),
      })
      .pipe(catchError(this.apiErrorHandler));
  }

  private apiErrorHandler(error: HttpErrorResponse) {
    console.error('Backend error:', error);
    return throwError(() => error);
  }
}
