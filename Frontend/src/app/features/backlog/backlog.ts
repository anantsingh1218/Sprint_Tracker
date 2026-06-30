import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';

import { Priority, WorkItem, WorkItemType } from '../../models/workItem';
import { WorkItemService } from '../../services/workItemService';
import { fadeSlide } from '../../animations/workItemAnimations';
import { FeatureOverlay } from '../feature-overlay/feature-overlay';
import { Story } from '../story/story';
import { IFeature } from '../../models/featureInterface';
import { IStory } from '../../models/storyInterface';
import { ApiService } from '../../core/apiService/api-service';
import { IFeatureResponse } from '../../models/featureResponseInterface';
import { IStoryResponse } from '../../models/storyResponseInterface';
import { forkJoin } from 'rxjs';
import { ITasksResponse } from '../../models/taskResponseInterface';
import { ITask } from '../../models/taskInterface';
import { Tasks } from '../tasks/tasks';
import { IBug } from '../../models/bugInterface';
import { IBugResponse } from '../../models/bugResponseInterface';
import { Bug } from '../bug/bug';

interface TreeNode extends WorkItem {
  children: TreeNode[];
  expanded?: boolean;
}

@Component({
  selector: 'app-backlog',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatMenuModule,
    FeatureOverlay,
    Story,
    Tasks,
    Bug,
  ],
  templateUrl: './backlog.html',
  styleUrls: ['./backlog.css'],
  animations: [fadeSlide],
})
export class Backlog {
  readonly WorkItemType = WorkItemType;

  tree: TreeNode[] = [];

  selectedFeature: IFeature | null = null;
  selectedStory: IStory | null = null;
  selectedTask: ITask | null = null;
  selectedBug: IBug | null = null;
  parentItem: WorkItem | null = null;
  overlayType: WorkItemType | null = null;
  isOverlayOpen = false;
  features: IFeatureResponse[] = [];
  stories: IStoryResponse[] = [];
  tasks: ITasksResponse[] = [];
  bugs: IBugResponse[] = [];

  constructor(
    private service: WorkItemService,
    private apiService: ApiService,
    private cdr: ChangeDetectorRef,
  ) {
    this.getBacklog();
  }

  // ---------- TREE ----------
  buildTree(items: WorkItem[]): TreeNode[] {
    const map = new Map<string, TreeNode>();

    items.forEach((i) => {
      map.set(i.id, { ...i, children: [], expanded: true });
    });

    const roots: TreeNode[] = [];

    map.forEach((node) => {
      if (node.parentId) {
        const parent = map.get(node.parentId);
        if (parent) parent.children.push(node);
        else roots.push(node);
      } else {
        roots.push(node);
      }
    });

    return roots;
  }

  toggle(node: TreeNode) {
    node.expanded = !node.expanded;
    this.tree = [...this.tree];
  }

  // ---------- CREATE ----------
  openCreate(type: WorkItemType, parent: WorkItem | null = null) {
    this.overlayType = type;
    this.parentItem = parent;

    const draft = this.service.createEmptyItem(type, parent?.id ?? null);

    switch (type) {
      case WorkItemType.Feature:
        this.selectedFeature = this.toFeature(draft);
        break;
      case WorkItemType.Story:
        this.selectedStory = this.toStory(draft);
        break;
      case WorkItemType.Task:
        this.selectedTask = this.toTask(draft);
        break;
      case WorkItemType.Bug:
        this.selectedBug = this.toBug(draft);
        break;
    }

    this.isOverlayOpen = true;
  }

  // ---------- EDIT ----------
  openEdit(item: WorkItem) {
    this.overlayType = item.type;
    this.parentItem = this.service.items.find((i) => i.id === item.parentId) ?? null;

    const fresh = this.service.items.find((i) => i.id === item.id) ?? item;

    switch (item.type) {
      case WorkItemType.Feature:
        this.selectedFeature = this.toFeature(fresh);
        break;
      case WorkItemType.Story:
        this.selectedStory = this.toStory(fresh);
        break;
      case WorkItemType.Task:
        this.selectedTask = this.toTask(fresh);
        break;
      case WorkItemType.Bug:
        this.selectedBug = this.toBug(fresh);
        break;
    }
    this.isOverlayOpen = true;
  }

  closeOverlay() {
    this.resetOverlayState();
  }

  // ---------- SAVE FEATURE ----------
  saveFeature(updated: IFeature) {
    const workItem: WorkItem = this.fromFeature(updated);
    this.save(workItem);
  }

  // ---------- SAVE STORY ----------
  saveStory(updated: IStory) {
    const workItem: WorkItem = this.fromStory(updated);
    this.save(workItem);
  }

  saveTask(updated: ITask) {
    const workItem: WorkItem = this.fromTask(updated);
    this.save(workItem);
  }

  saveBug(updated: IBug) {
    const workItem: WorkItem = this.fromBug(updated);
    this.save(workItem);
  }

  // ---------- CORE SAVE ----------
  private save(item: WorkItem) {
    if (!item.title?.trim()) return;

    const items = [...this.service.items];

    const finalItem: WorkItem = {
      ...item,
      parentId: this.parentItem?.id ?? item.parentId ?? null,
    };

    const index = items.findIndex((i) => i.id === finalItem.id);

    if (index >= 0) items[index] = finalItem;
    else items.push(finalItem);

    this.service.update(items);

    this.closeOverlay();
  }

  private extractNumericId(id: string | null): number | null {
    if (!id) return null;
    return Number(id.replace(/^[A-Za-z]+/, ''));
  }

  // ---------- MAPPERS ----------
  toFeature(item: WorkItem): IFeature {
    return {
      id: item.id,
      title: item.title,
      description: item.description,
      status: item.status,
      priority: item.priority,
      estimatedStoryPoints: item.estimatedPoints,
      remainingStoryPoint: item.remainingPoints,
      productId: item.productCategory,
      sprintId: item.sprintName,
      userId: item.assignedTo,
      comments: item.comments,
    };
  }

  toStory(item: WorkItem): IStory {
    return {
      id: item.id,
      title: item.title,
      body: item.description,
      status: item.status,
      priority: item.priority,
      estimatedStoryPoints: item.estimatedPoints,
      remainingStoryPoint: item.remainingPoints,
      featureId: item.parentId ? this.extractNumericId(item.parentId) : null,
      sprintId: item.sprintName,
      userId: item.assignedTo,
      comments: item.comments,
    };
  }

  toTask(item: WorkItem): ITask {
    return {
      id: item.id,
      title: item.title,
      description: '',
      status: item.status,
      priority: item.priority,
      estimatedHours: item.estimatedPoints,
      remainingHours: item.remainingPoints,
      storyId: item.parentId ? this.extractNumericId(item.parentId) : null,
      sprintId: item.sprintName,
      userId: item.assignedTo,
      comments: item.comments,
    };
  }

  toBug(item: WorkItem): IBug {
    return {
      id: item.id,
      title: item.title,
      description: '',
      status: item.status,
      priority: item.priority,
      estimatedHours: 0,
      remainingHours: 0,
      reopenCount: 0,
      storyId: item.parentId ? this.extractNumericId(item.parentId) : null,
      sprintId: null,
      assignedTo: null,
      comments: [],
    };
  }

  fromFeature(f: IFeature): WorkItem {
    return {
      id: f.id,
      title: f.title,
      type: WorkItemType.Feature,
      parentId: null,
      status: f.status,
      description: f.description,
      sprintName: f.sprintId,
      priority: f.priority,
      assignedTo: f.userId,
      productCategory: f.productId,
      reopenCount: 0,
      estimatedPoints: f.estimatedStoryPoints,
      remainingPoints: f.remainingStoryPoint,
      comments: f.comments,
    };
  }

  fromStory(s: IStory): WorkItem {
    return {
      id: s.id,
      title: s.title,
      type: WorkItemType.Story,
      parentId: s.featureId ? `F${s.featureId}` : null,
      status: s.status,
      description: s.body,
      sprintName: s.sprintId,
      priority: s.priority,
      assignedTo: s.userId,
      productCategory: null,
      reopenCount: 0,
      estimatedPoints: s.estimatedStoryPoints,
      remainingPoints: s.remainingStoryPoint,
      comments: s.comments,
    };
  }

  fromTask(t: ITask): WorkItem {
    return {
      id: t.id,
      title: t.title,
      type: WorkItemType.Task,
      parentId: t.storyId ? `S${t.storyId}` : null,
      status: t.status,
      description: t.description,
      sprintName: t.sprintId,
      priority: t.priority,
      assignedTo: t.userId,
      productCategory: null,
      reopenCount: 0,
      estimatedPoints: t.estimatedHours,
      remainingPoints: t.remainingHours,
      comments: t.comments,
    };
  }

  fromBug(b: IBug): WorkItem {
    return {
      id: b.id,
      title: b.title,
      type: WorkItemType.Bug,
      parentId: b.storyId ? `S${b.storyId}` : null,
      status: b.status,
      description: b.description,
      sprintName: b.sprintId,
      priority: b.priority,
      assignedTo: b.assignedTo,
      productCategory: null,
      reopenCount: b.reopenCount,
      estimatedPoints: b.estimatedHours,
      remainingPoints: b.remainingHours,
      comments: b.comments,
    };
  }

  private resetOverlayState() {
    this.isOverlayOpen = false;
    this.overlayType = null;
    this.parentItem = null;

    this.selectedFeature = null;
    this.selectedStory = null;
    this.selectedTask = null;
    this.selectedBug = null;
  }

  private mapFeature(feature: IFeatureResponse): WorkItem {
    return {
      id: `F${feature.id}`,
      title: feature.title,
      type: WorkItemType.Feature,
      parentId: null,
      status: feature.featureStatus,
      description: feature.description,
      sprintName: feature.sprintName,
      priority: feature.priority,
      assignedTo: feature.assignedTo,
      productCategory: feature.productName,
      reopenCount: 0,
      estimatedPoints: feature.estimatedStoryPoints,
      remainingPoints: feature.remainingStoryPoints,
      comments: [],
    };
  }

  private mapStory(story: IStoryResponse): WorkItem {
    return {
      id: `S${story.id}`,
      title: story.title,
      type: WorkItemType.Story,
      parentId: `F${story.featureId}`,
      status: story.storyStatus,
      description: story.description,
      sprintName: story.sprintName,
      priority: story.storyPriority,
      assignedTo: story.assignedTo,
      productCategory: null,
      reopenCount: 0,
      estimatedPoints: story.estimatedStoryPoints,
      remainingPoints: story.remainingStoryPoints,
      comments: [],
    };
  }

  private mapTask(task: ITasksResponse): WorkItem {
    return {
      id: `T${task.id}`,
      title: task.title,
      type: WorkItemType.Task,
      parentId: `S${task.storyId}`,
      status: task.taskStatus,
      description: task.description,
      sprintName: task.sprintName,
      priority: task.taskPriority,
      assignedTo: task.assignedTo,
      productCategory: null,
      reopenCount: 0,
      estimatedPoints: task.estimatedHours,
      remainingPoints: task.remainingHours,
      comments: [],
    };
  }

  private mapBug(bug: IBugResponse): WorkItem {
    return {
      id: `B${bug.id}`,
      title: bug.title,
      type: WorkItemType.Bug,
      parentId: `S${bug.storyId}`,
      status: bug.bugStatus,
      description: bug.description,
      sprintName: bug.sprintName,
      priority: bug.bugPriority,
      assignedTo: bug.assignedTo,
      productCategory: null,
      reopenCount: bug.reopenCount,
      estimatedPoints: bug.estimatedHours,
      remainingPoints: bug.remainingHours,
      comments: [],
    };
  }

  private buildTreeWithChildren(
    features: WorkItem[],
    stories: WorkItem[],
    tasks: WorkItem[],
    bugs: WorkItem[],
  ): TreeNode[] {
    // 1. Create a universal lookup map
    const map = new Map<string, TreeNode>();

    // 2. Add Features to the map
    features.forEach((f) => {
      map.set(this.normalizeId(f.id), {
        ...f,
        children: [],
        expanded: true,
      });
    });

    // 3. Add Stories to the map AND push them to their Feature parents
    stories.forEach((s) => {
      const normalizedStoryId = this.normalizeId(s.id);
      const parentId = this.normalizeId(s.parentId);

      // Create the story node shell in our map so its upcoming tasks can find it
      const storyNode: TreeNode = {
        ...s,
        children: [],
        expanded: false,
      };
      map.set(normalizedStoryId, storyNode);

      // Attach story node to its feature parent
      const parentFeature = map.get(parentId);
      if (parentFeature) {
        parentFeature.children.push(storyNode);
      }
    });

    // 4. Push Tasks to their Story parents (which now confidently exist in the map!)
    tasks.forEach((t) => {
      const parentId = this.normalizeId(t.parentId);
      const parentStory = map.get(parentId);

      if (parentStory) {
        parentStory.children.push({
          ...t,
          children: [],
          expanded: false,
        });
      } else {
        console.warn(
          `Orphaned task detected: Task ${t.id} looks for parent ${t.parentId} but it wasn't found.`,
        );
      }
    });

    // 5. Push Bugs to their Story parents (which now confidently exist in the map!)
    bugs.forEach((b) => {
      const parentId = this.normalizeId(b.parentId);
      const parentStory = map.get(parentId);

      if (parentStory) {
        parentStory.children.push({
          ...b,
          children: [],
          expanded: false,
        });
      } else {
        console.warn(
          `Orphaned task detected: Bug ${b.id} looks for parent ${b.parentId} but it wasn't found.`,
        );
      }
    });

    // 6. Return ONLY the root level nodes (Features)
    // We filter the full map values down to items that have no parentId
    return Array.from(map.values()).filter((node) => node.parentId === null);
  }

  private getBacklog() {
    forkJoin({
      features: this.apiService.getRequest<IFeatureResponse[]>('/backlog/getAllFeatures'),
      stories: this.apiService.getRequest<IStoryResponse[]>('/backlog/getAllStories'),
      tasks: this.apiService.getRequest<ITasksResponse[]>('/backlog/getAllTasks'),
      bugs: this.apiService.getRequest<IBugResponse[]>('/backlog/getAllBugs'),

    }).subscribe({
      next: ({ features, stories, tasks, bugs }) => {
        const featureNodes = features.map((f) => this.mapFeature(f));
        const storyNodes = stories.map((s) => this.mapStory(s));
        const taskNodes = tasks.map((t) => this.mapTask(t));
        const bugNodes = bugs.map((b) => this.mapBug(b));

        const allItems: WorkItem[] = [...featureNodes, ...storyNodes, ...taskNodes, ...bugNodes]

        this.service.update(allItems)

        // Overwrite tree variable with a brand new array reference
        this.tree = [...this.buildTreeWithChildren(featureNodes, storyNodes, taskNodes, bugNodes)];

        // Force Angular to scan and render the UI now that asynchronous data is ready
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error fetching backlog data', err);
      },
    });
  }

  private normalizeId(id: any): string {
    return String(id).replace(/\[|\]/g, '').trim();
  }
}
