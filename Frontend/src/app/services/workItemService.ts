import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Priority, WorkItem, WorkItemType, WorkStatus } from '../models/workItem';
import { IComment } from '../models/storyInterface';
import { ApiService } from '../core/apiService/api-service';

@Injectable({ providedIn: 'root' })
export class WorkItemService {
  private endpointMap: { [key: string]: string } = {
    F: 'feature',
    S: 'story',
    T: 'task',
    B: 'Bug',
    SP: 'sprint', // Example extra
  };
  constructor(private apiService: ApiService) {
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
  deleteByCode(combinedCode: string): Observable<any> {
    // 1. Parse the string using the RegEx
    const match = combinedCode.match(/^([A-Za-z]+)(\d+)$/);

    if (!match) {
      throw new Error(`Invalid work item code format: ${combinedCode}`);
    }

    const prefix = match[1].toUpperCase(); // Ensure uppercase matching (e.g., "s" -> "S")
    const id = parseInt(match[2], 10);

    // 2. Get the matching endpoint path
    const resource = this.endpointMap[prefix];

    if (!resource) {
      throw new Error(`No API endpoint mapped for prefix: ${prefix}`);
    }

    // 3. Construct the URL (e.g., "https://api.yourdomain.com/api/stories/3")
    const deleteUrl = `/${resource}/${combinedCode}`;
    console.log(deleteUrl);
    // 4. Return the HTTP Delete Observable
    return this.apiService.deleteRequest(deleteUrl);
  }
}
