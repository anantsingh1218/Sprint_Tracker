import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BugService } from '../bug/bug.service';
import { Bug } from '../bug/bug';

@Component({
  selector: 'app-bug-list',
  standalone: true,
  imports: [CommonModule, Bug],
  templateUrl: './bug-list.html',
  styleUrl: './bug-list.css'
})
export class BugList implements OnInit {

  bugs: any[] = [];
  isBugOpen = false;
  selectedBug: any;

  constructor(private bugService: BugService) {}

  ngOnInit() {
    this.loadBugs();
  }

  loadBugs() {
    this.bugService.getAllBugs().subscribe({
      next: (res: any) => {
        console.log("Fetched Bugs:", res);
        this.bugs = res;
      },

      error: (err: any) => {
        console.error("Fetch Error:", err);
      }
    });
  }

  openBug(bug: any) {
    this.selectedBug = { ...bug };
    this.isBugOpen = true;
  }

  openCreateBug() {
   this.selectedBug = {
  title: '',
  description: '',
  bugstatus: '',
  priority: '',
  assignedto: 0,
  sprintid: 0,
  storyid: 0,
  originalestimatehours: 0,
  remainingestimatehours: 0,
  comments: ''
};
    this.isBugOpen = true;
  }

  saveBug(bug: any) {
    this.bugService.createBug(bug).subscribe({
      next: (res: any) => {
        console.log("Bug Created:", res);
        this.loadBugs();
        this.closeBug();
      },

      error: (err: any) => {
        console.error("Create Error:", err);
      }
    });
  }

  deleteBug(id: number) {
    this.bugService.deleteBug(id).subscribe({
      next: () => {
        this.loadBugs();
      },

      error: (err: any) => {
        console.error("Delete Error:", err);
      }
    });
  }

  closeBug() {
    this.isBugOpen = false;
  }
}
