import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Priority, WorkItem, WorkItemType, WorkStatus } from '../models/workItem';
import { IComment } from '../models/storyInterface';

@Injectable({ providedIn: 'root' })
export class WorkItemService {
  constructor() {
    this.syncCountersFromItems(this.itemsSubject.value);
  }

  private counters: Record<WorkItemType, number> = {
    Feature: 0,
    Story: 0,
    Task: 0,
    Bug: 0,
  };
  
  private itemsSubject = new BehaviorSubject<WorkItem[]>([]);

  items$ = this.itemsSubject.asObservable();

  get items(): WorkItem[] {
    return this.itemsSubject.value as WorkItem[];
  }

  private generateId(type: WorkItemType): string {
    this.syncCountersFromItems(this.itemsSubject.value);
    const prefixMap: Record<WorkItemType, string> = {
      [WorkItemType.Feature]: 'F',
      [WorkItemType.Story]: 'S',
      [WorkItemType.Task]: 'T',
      [WorkItemType.Bug]: 'B',
    };

    this.counters[type]++;

    return `${prefixMap[type]}${this.counters[type]}`;
  }

  update(items: WorkItem[]) {
    this.itemsSubject.next(items);
    // this.syncCountersFromItems(items);
  }

  updateStatus(id: string, status: any) {
    const updated = this.items.map((i) => (i.id === id ? { ...i, status } : i));
    this.update(updated);
  }

  private syncCountersFromItems(items: WorkItem[]) {
    const prefixMap: Record<WorkItemType, string> = {
      [WorkItemType.Feature]: 'F',
      [WorkItemType.Story]: 'S',
      [WorkItemType.Task]: 'T',
      [WorkItemType.Bug]: 'B',
    };

    // reset
    this.counters = {
      [WorkItemType.Feature]: 0,
      [WorkItemType.Story]: 0,
      [WorkItemType.Task]: 0,
      [WorkItemType.Bug]: 0,
    };

    for (const item of items) {
      const prefix = prefixMap[item.type];
      const num = parseInt(item.id.replace(prefix, ''), 10);

      if (!isNaN(num)) {
        this.counters[item.type] = Math.max(this.counters[item.type], num);
      }
    }
  }

  addItem(item: WorkItem) {
    const newItem: WorkItem = {
      ...item,
      id: item.id?.trim() ? item.id : this.generateId(item.type),
    };

    const updated = [...this.items, newItem];
    this.update(updated);
  }

  createEmptyItem(type: WorkItemType, parentId: string | null = null): WorkItem {
    return {
      id: null as unknown as string,
      title: 'New Work Item',
      type,
      parentId,
      status: WorkStatus.OPEN,
      description: '',
      sprintName: '',
      priority: Priority.LOW,
      assignedTo: '',
      productCategory: '',
      reopenCount: 0,
      estimatedPoints: 0,
      remainingPoints: 0,
      comments: [],
    };
  }
}
