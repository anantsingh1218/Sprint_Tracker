import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { WorkItem, WorkItemType } from '../models/workItem';

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
  private itemsSubject = new BehaviorSubject<WorkItem[]>([
    {
      id: 'F1',
      title: 'User Management',
      type: WorkItemType.Feature,
      status: 'todo',
      parentId: null,
    },
    {
      id: 'S1',
      title: 'Login System',
      type: WorkItemType.Story,
      parentId: 'F1',
      status: 'in-progress',
    },
    {
      id: 'T1',
      title: 'Build login UI',
      type: WorkItemType.Task,
      parentId: 'S1',
      status: 'done',
    },
  ]);

  items$ = this.itemsSubject.asObservable();

  get items(): WorkItem[] {
    return this.itemsSubject.value as WorkItem[];
  }

  private generateId(type: WorkItemType): string {
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
    this.syncCountersFromItems(items);
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
      // id: '',
      id: this.generateId(type),
      title: 'New Feature',
      type,
      parentId,
      status: 'todo',
    };
  }
}
