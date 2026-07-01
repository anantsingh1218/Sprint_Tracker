import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { UpperCasePipe } from '@angular/common';
import { ApiService } from '../../core/apiService/api-service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [UpperCasePipe],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit {
 profile: any;

 constructor(private apiService: ApiService, private cdr: ChangeDetectorRef){}

 ngOnInit(){
   this.loadProfile();
 }

 loadProfile(){
  this.apiService.getRequest<any>('/profile').subscribe({
    next: (res: any)=>{
      this.profile = typeof res === 'string' ? JSON.parse(res) : res;
      this.cdr.markForCheck();
    },
    error:(err:any)=>{
      console.error("Profile Fetch Error:",err);
    }
  });
 }

}
