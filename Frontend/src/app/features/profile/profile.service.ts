import { Injectable} from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable} from "rxjs";

@Injectable({
  providedIn:'root'
})
export class ProfileService{
  private apiUrl='http://localhost:8080/profile';

  constructor(private http: HttpClient){}

  getProfile(): Observable<any>{
    const token=localStorage.getItem('jwtToken')

    return this.http.get(this.apiUrl,{
      headers:{
        Authorization:`Bearer ${token}`
      }
    });
  }

}
