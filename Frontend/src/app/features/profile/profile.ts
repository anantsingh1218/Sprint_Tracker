import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { UpperCasePipe } from '@angular/common';
import { ApiService } from '../../core/apiService/api-service';
import { ProfileService } from './profile.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [UpperCasePipe],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit {
 profile: any;

 constructor(private pf:ProfileService, private cdr: ChangeDetectorRef){}

 ngOnInit(){
   this.loadProfile();
 }

 loadProfile(){
  this.pf.getProfile().subscribe({
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
