import { Injectable } from '@angular/core';
import { ApiService } from '../../core/apiService/api-service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FeatureService {
  constructor(private apiService: ApiService) {}

  getAllFeatures(): Observable<any[]> {
    return this.apiService.getRequest<any[]>('/feature/all');
  }

  addFeature(feature: any): Observable<any> {
    return this.apiService.postRequest<any>('/feature/add', feature);
  }

  getFeatureById(featureCode: string): Observable<any> {
    return this.apiService.getRequest<any>(`/feature/${featureCode}`);
  }

  updateFeature(updatedFeature: any): Observable<any> {
    return this.apiService.putRequest<any>(`/feature/${updatedFeature.featureCode}`, updatedFeature);
  }
}
