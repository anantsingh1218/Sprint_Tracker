import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DsuService } from './dsu.service';

@Component({
  selector: 'app-dsu',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dsu.html',
  styleUrls: ['./dsu.css']
})
export class DsuComponent {

  entityType: string = '';
  entityId: number = 0;

autoStartDate: string = '';
autoEndDate: string = '';
autoDsuData: any = null;

  dsuData = {
    notesdate: '',
    status: '',
    completedwork: '',
    blockers: '',
    nextplan: ''
  };

  reportDate: string = '';
  reportData: any = null;
  successMessage: string = '';

  constructor(
    private dsuService: DsuService,
    private cdr: ChangeDetectorRef
  ) {}

  submitDSU() {
  this.dsuService.createDSU(
    this.entityType,
    this.entityId,
    this.dsuData
  ).subscribe({
    next: (res: any) => {
      console.log('DSU Created:', res);

      // Clear form first
      this.entityType = '';
      this.entityId = 0;
      this.dsuData = {
        notesdate: '',
        status: '',
        completedwork: '',
        blockers: '',
        nextplan: ''
      };

      // Show success message after render cycle
      setTimeout(() => {
        this.successMessage = 'DSU Created Successfully';
        this.cdr.detectChanges();
      }, 100);

      // Remove success message after 3 seconds
      setTimeout(() => {
        this.successMessage = '';
        this.cdr.detectChanges();
      }, 3000);
    },

    error: (err: any) => {
      console.error('Error creating DSU:', err);

      this.successMessage = 'Failed to create DSU';
      this.cdr.detectChanges();

      // Remove error message after 3 seconds
      setTimeout(() => {
        this.successMessage = '';
        this.cdr.detectChanges();
      }, 3000);
    }
  });
}

  fetchReport() {
    this.dsuService.getReport(this.reportDate)
      .subscribe({
        next: (res: any) => {
          this.reportData = res;
          console.log('DSU Report:', res);
        },

        error: (err: any) => {
          console.error('Error fetching report:', err);
        }
      });
  }


  generateAutoDSU() {
  this.dsuService.generateAutoDSU(
    this.autoStartDate,
    this.autoEndDate
  ).subscribe({
    next: (res: any) => {
      this.autoDsuData = res;
      console.log('Auto DSU:', res);
    },
    error: (err: any) => {
      console.error('Error generating Auto DSU:', err);
    }
  });
}
}
