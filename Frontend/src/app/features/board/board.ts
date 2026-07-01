import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DragDropModule, CdkDragDrop, transferArrayItem } from '@angular/cdk/drag-drop';
import { MatCardModule } from '@angular/material/card';

import { WorkItem, WorkStatus } from '../../models/workItem';
import { WorkItemService } from '../../services/workItemService';
import { fadeSlide } from '../../animations/workItemAnimations';

@Component({
  selector: 'app-board',
  standalone: true,
  imports: [
    CommonModule,
    DragDropModule,
    MatCardModule
  ],
  templateUrl: './board.html',
  styleUrls: ['./board.css'],
  animations: [fadeSlide]
})
export class Board {

  todo: WorkItem[] = [];
  inProgress: WorkItem[] = [];
  done: WorkItem[] = [];

  constructor(private service: WorkItemService) {
    this.service.items$.subscribe(items => {
      this.todo = items.filter(i => i.status === WorkStatus.OPEN);
      this.inProgress = items.filter(i => i.status === WorkStatus.IN_PROGRESS);
      this.done = items.filter(i => i.status === WorkStatus.DONE);
    });
  }

  drop(event: CdkDragDrop<WorkItem[]>, status: any) {
    if (event.previousContainer === event.container) return;

    const item = event.previousContainer.data[event.previousIndex];

    this.service.updateStatus(item.id, status);

    transferArrayItem(
      event.previousContainer.data,
      event.container.data,
      event.previousIndex,
      event.currentIndex
    );
  }
}