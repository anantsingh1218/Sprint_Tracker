import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiService } from '../core/apiService/api-service';
import { DashboardResponse } from '../features/dashboard/models/dashboard.model';
import { ProductDropdown } from '../features/dashboard/models/dashboard.model';
import { Burndown } from '../features/dashboard/models/dashboard.model';
import { TeamCapacity } from '../features/dashboard/models/dashboard.model';
import { ReleaseReadiness } from '../features/dashboard/models/dashboard.model';
import { Velocity } from '../features/dashboard/models/dashboard.model';


@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(
    private api: ApiService
  ) {}

  getDashboard(userId: number): Observable<DashboardResponse> {

    return this.api.getRequest<DashboardResponse>(
      `/dashboard/${userId}`
    );

  }

  getProducts(userId: number): Observable<ProductDropdown[]> {

    return this.api.getRequest<ProductDropdown[]>(
        `/dashboard/products/${userId}`
    );

}

getBurndown(
    userId:number,
    productId:number
){

    return this.api.getRequest<Burndown>(
        `/dashboard/burndown/${userId}`,
        {
            productId
        }
    );

}
getTeamCapacity(
    userId:number,
    productId:number
){

    return this.api.getRequest<TeamCapacity>(
        `/dashboard/team-capacity/${userId}`,
        {
            productId
        }
    );

}

getReleaseReadiness(
    userId:number,
    productId:number
){

    return this.api.getRequest<ReleaseReadiness>(
        `/dashboard/release/${userId}`,
        {
            productId
        }
    );

}

getVelocity(
    userId: number,
    productId: number
){

    return this.api.getRequest<Velocity>(
        `/dashboard/velocity/${userId}`,
        {
            productId
        }
    );

}

}