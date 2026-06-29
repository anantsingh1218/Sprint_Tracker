import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { WorkItemType } from '../../models/workItem';
import { Attachment } from '../../models/attachmentInterface';
import { FetchAttachmentsResponse } from '../../models/fetchAttachmnetResponseInterface';
import { AuthService } from '../auth/auth';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private apiUrl = 'http://localhost:8080';

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {}

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
    let headers = new HttpHeaders();
    const jwtToken: string | null = this.authService.getToken();

    if (jwtToken) {
      headers = headers.set('Authorization', 'Bearer ' + jwtToken);
    }

    const isFormData = payload instanceof FormData;

    if (!isFormData) {
      headers = headers.set('Content-Type', contentType);
    }

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

  // deleteRequest<T>(endpoint: string, requestParams?: Record<string, any>): Observable<T> {
  //   return this.http
  //     .delete<T>(`${this.apiUrl}${endpoint}`, {
  //       params: this.buildParams(requestParams),
  //     })
  //     .pipe(catchError(this.apiErrorHandler));
  // }
  deleteRequest<T>(
    endpoint: string,
    options?: {
      params?: Record<string, any>;
      body?: any;
    },
  ): Observable<T> {
    return this.http.delete<T>(`${this.apiUrl}${endpoint}`, {
      params: this.buildParams(options?.params),
      body: options?.body ?? null,
    });
  }

  private apiErrorHandler(error: HttpErrorResponse) {
    console.error('Backend error:', error);
    return throwError(() => error);
  }

  getAttachments(entityType: WorkItemType, entityId: number) {
    return this.getRequest<FetchAttachmentsResponse>(
      `/attachment/fetch/${entityType.toUpperCase()}/${entityId}`,
    );
  }

  uploadAttachments(entityType: WorkItemType, entityId: number, files: File[]) {
    const formData = new FormData();
    files.forEach((file) => {
      formData.append('attachments', file); // same key repeated
    });
    return this.postRequest<Array<Attachment>>(
      `/attachment/upload/${entityType.toUpperCase()}/${entityId}`,
      formData,
    );
  }

  toApiWorkItemId(id: string): number {
    const match = id.match(/^([A-Z])(\d+)$/);

    if (!match) throw new Error(`Invalid WorkItem id: ${id}`);

    return Number(match[2]);
  }
}
