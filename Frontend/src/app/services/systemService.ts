import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiService } from '../core/apiService/api-service';
import { SystemStatus } from '../models/systemStatusInterface';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class SystemService {
  constructor(
    private http: HttpClient,
    private apiService: ApiService,
  ) {
  }

  getStatus() {
    return this.apiService.getRequest<SystemStatus>('/system/status');
  }

  private statusSubject = new BehaviorSubject<SystemStatus | null>(null);
  status$ = this.statusSubject.asObservable();

  loadStatus() {
  this.apiService.getRequest<SystemStatus>('/system/status')
    .subscribe(status => {
      this.statusSubject.next(status);
    });
}
}
