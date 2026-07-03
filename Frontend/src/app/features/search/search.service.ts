import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn:'root'
})
export class SearchService {
  private baseUrl ='http://localhost:8080';

  constructor(private http: HttpClient){}

  private getHeaders(){
    const token = localStorage.getItem('jwtToken');

    return {
      Authorization: `Bearer ${token}`
    };
  }

  search(keyword: string) {
  const token = localStorage.getItem('jwtToken');

  return this.http.get<any[]>(
    `${this.baseUrl}/search?q=${keyword}`,
    {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }
  );
}
}
