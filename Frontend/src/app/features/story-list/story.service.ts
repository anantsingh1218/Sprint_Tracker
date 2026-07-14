import { Injectable } from "@angular/core";
import { HttpClient,HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class StoryService{
  private baseUrl='http://localhost:8080/story';

  constructor(private http:HttpClient){}

  private getHeaders(): HttpHeaders{
    const token=localStorage.getItem('jwtToken');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getStories(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/all`, {
      headers: this.getHeaders()
    });
  }

  createStory(storyData: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/create`, storyData, {
      headers: this.getHeaders()
    });
  }

  updateStory(id:number,storyData:any):Observable<any>{
   return this.http.put<any>(`${this.baseUrl}/${id}`,storyData,{headers: this.getHeaders()});
  }


getUsersDropdown(): Observable<any[]> {
  return this.http.get<any[]>('http://localhost:8080/users/all', { headers: this.getHeaders() });
}

getFeaturesDropdown(): Observable<any[]> {
  return this.http.get<any[]>('http://localhost:8080/feature/all', { headers: this.getHeaders() });
}

getSprintsDropdown(): Observable<any[]> {
  return this.http.get<any[]>('http://localhost:8080/sprint/all', { headers: this.getHeaders() });
}
}
