import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sprint',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './sprint.html',
  styleUrl: './sprint.css'
})
export class Sprint {

  sprintName = '';
  startDate = '';
  endDate = '';

  sprints: any[] = [];

  addSprint() {
    if (!this.sprintName) return;

    this.sprints.push({
      name: this.sprintName,
      start: this.startDate,
      end: this.endDate
    });

    this.sprintName = '';
    this.startDate = '';
    this.endDate = '';
  }
}
