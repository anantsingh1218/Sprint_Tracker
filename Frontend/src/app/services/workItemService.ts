import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { WorkItem } from '../models/workItem';

@Injectable({ providedIn: 'root' })
export class WorkItemService {

  private itemsSubject = new BehaviorSubject<WorkItem[]>([
    {
      id: 'F1',
      title: 'User Management',
      type: 'feature',
      status: 'todo',
      parentId: null
    },
    {
      id: 'S1',
      title: 'Login System',
      type: 'story',
      parentId: 'F1',
      status: 'in-progress'
    },
    {
      id: 'T1',
      title: 'Build login UI',
      type: 'task',
      parentId: 'S1',
      status: 'done'
    }
  ]);

  items$ = this.itemsSubject.asObservable();

  get items(): WorkItem[] {
    return this.itemsSubject.value;
  }

  update(items: WorkItem[]) {
    this.itemsSubject.next(items);
  }

  updateStatus(id: string, status: any) {
    const updated = this.items.map(i =>
      i.id === id ? { ...i, status } : i
    );
    this.update(updated);
  }
}