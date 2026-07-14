import { Component, ChangeDetectorRef, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // <-- Required for ngModel filtering
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
import { IBug } from '../../models/bugInterface';
import { IBugResponse } from '../../models/bugResponseInterface';
import { Bug } from '../bug/bug';
import { TaskOverlay } from '../task-overlay/task-overlay';

interface TreeNode extends WorkItem {
  children: TreeNode[];
  expanded?: boolean;
}

@Component({
  selector: 'app-integrated-view',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule, // <-- Added here
    MatCardModule,
    MatButtonModule,
    MatMenuModule,
    FeatureOverlay,
    Story,
    TaskOverlay,
    Bug,
  ],
  templateUrl: './integrated-view.html',
  styleUrls: ['./integrated-view.css'],
  animations: [fadeSlide],
})
export class IntegrateView {
  readonly WorkItemType = WorkItemType;

  tree: TreeNode[] = [];
  filteredTree: TreeNode[] = []; // <-- Render this in the HTML instead of 'tree'

  // --- FILTER BINDINGS ---
  searchTerm: string = '';
  selectedProduct: string = '';
  selectedSprint: string = '';
  selectedStatus: string = '';
  selectedUser: string = '';

  // --- SIGNALS FOR DROPDOWNS ---
  usersList = signal<any[]>([]);
  featuresList = signal<any[]>([]);
  storyList = signal<any[]>([]);
  sprintsList = signal<any[]>([]);

  // --- DYNAMIC UNIQUE OPTION LISTS FOR FILTERS ---
  uniqueProducts: string[] = [];
  uniqueSprints: string[] = [];
  uniqueStatuses: string[] = [];
  uniqueUsers: string[] = [];
  // uniqueProducts = computed(() => {
  //   const products = this.service.items
  //     .map(item => item.productCategory)
  //     .filter((val): val is NonNullable<typeof val> => val != null);
  //   return Array.from(new Set(products)).sort();
  // });

  // uniqueSprints = computed(() => {
  //   const sprints = this.service.items
  //     .map(item => item.sprintName)
  //     .filter((val): val is NonNullable<typeof val> => val != null);
  //   return Array.from(new Set(sprints)).sort();
  // });

  // uniqueStatuses = computed(() => {
  //   const statuses = this.service.items
  //     .map(item => item.status)
  //     .filter((val): val is NonNullable<typeof val> => val != null);
  //   return Array.from(new Set(statuses)).sort();
  // });

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
    public service: WorkItemService, // set to public to read from template/computed values safely
    private apiService: ApiService,
    private cdr: ChangeDetectorRef,
  ) {
    this.getBacklog();
  }

  // ---------- TREE & FILTERING ----------
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
    // Mirror structural toggle directly in both arrays
    const findAndToggle = (list: TreeNode[]) => {
      for (let item of list) {
        if (item.id === node.id) {
          item.expanded = node.expanded;
          return true;
        }
        if (item.children && findAndToggle(item.children)) return true;
      }
      return false;
    };
    findAndToggle(this.tree);
    this.applyFilters();
  }

  /**
   * Applies all filters on the base hierarchical tree.
   * Keeps parent structures intact if a child node matches the filters.
   */
applyFilters() {
    const hasActiveFilters = 
      !!this.searchTerm.trim() || 
      !!this.selectedProduct || 
      !!this.selectedSprint || 
      !!this.selectedStatus ||
      !!this.selectedUser; // <-- Added user check

    if (!hasActiveFilters) {
      this.filteredTree = JSON.parse(JSON.stringify(this.tree));
      this.cdr.detectChanges();
      return;
    }

    const filterNode = (nodes: TreeNode[], parentProduct: string | null = null): TreeNode[] => {
      return nodes
        .map((node): TreeNode | null => {
          const currentProduct = node.type === WorkItemType.Feature ? node.productCategory : parentProduct;
          const filteredChildren = filterNode(node.children || [], currentProduct);

          const matchesSearch = !this.searchTerm.trim() || 
            node.title.toLowerCase().includes(this.searchTerm.toLowerCase());
          
          const matchesSprint = !this.selectedSprint || 
            node.sprintName === this.selectedSprint;

          const matchesStatus = !this.selectedStatus || 
            node.status === this.selectedStatus;

          const matchesProduct = !this.selectedProduct || 
            currentProduct === this.selectedProduct;

          // --- New User Filter Check ---
          const matchesUser = !this.selectedUser || 
            node.assignedTo === this.selectedUser;

          const nodeSelfMatches = matchesSearch && matchesSprint && matchesStatus && matchesProduct && matchesUser;

          if (nodeSelfMatches || filteredChildren.length > 0) {
            return {
              ...node,
              children: filteredChildren,
              expanded: true 
            };
          }
          return null;
        })
        .filter((node): node is TreeNode => node !== null);
    };

    this.filteredTree = filterNode(this.tree);
    this.cdr.detectChanges();
  }

  resetFilters() {
    this.searchTerm = '';
    this.selectedProduct = '';
    this.selectedSprint = '';
    this.selectedStatus = '';
    this.selectedUser = ''; // <-- Reset user
    this.applyFilters();
  }

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

  saveFeature(updated: IFeature) {
    const workItem: WorkItem = this.fromFeature(updated);
    this.save(workItem);
  }

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

    const isNewItem: boolean = !item.id || item.id.trim() === '';

    if (isNewItem) {
      let endpoint: string = '';
      let payload: any;

      switch (item.type) {
        case WorkItemType.Feature:
          endpoint = '/feature/add';
          payload = this.toFeature(item);
          break;
        case WorkItemType.Story:
          endpoint = '/story/add';
          payload = this.toStory(item);
          break;
        case WorkItemType.Task:
          endpoint = '/task/add';
          payload = this.toTask(item);
          break;
        case WorkItemType.Bug:
          endpoint = '/bug/add';
          payload = this.toBug(item);
          break;
      }
      let {comments, ...destructedPayload} = payload;
      const payloadToSend = {...destructedPayload, comments: comments?.at(-1)?.text};
      console.log("To Save Payload = " + JSON.stringify(payloadToSend));
      this.apiService.postRequest<any>(endpoint, payloadToSend).subscribe({
        next: (response) => {
          let savedWorkItem: WorkItem;
          switch (item.type) {
            case WorkItemType.Feature:
              savedWorkItem = this.mapFeature(response);
              break;
            case WorkItemType.Story:
              savedWorkItem = this.mapStory(response);
              break;
            case WorkItemType.Task:
              savedWorkItem = this.mapTask(response);
              break;
            case WorkItemType.Bug:
              savedWorkItem = this.mapBug(response);
              break;
          }

          const currentItems = [...this.service.items, savedWorkItem];
          this.service.update(currentItems);

          this.refreshTree();
          this.closeOverlay();
        },
        error: (err) => console.error('Failed to create work item on backend', err),
      });
    } else {
      let endpoint = '';
      let payload: any;

      switch (item.type) {
        case WorkItemType.Feature:
          endpoint = `/feature/${item.id}`;
          payload = this.toFeature(item);
          break;
        case WorkItemType.Story:
          endpoint = `/story/${item.id}`;
          payload = this.toStory(item);
          break;
        case WorkItemType.Task:
          endpoint = `/task/${item.id}`;
          payload = this.toTask(item);
          break;
        case WorkItemType.Bug:
          endpoint = `/bug/${item.id}`;
          payload = this.toBug(item);
          break;
      }
      let {comments, ...destructedPayload} = payload;
      const payloadToSend = {...destructedPayload, comments: comments?.at(-1)?.text};
      console.log("To Save Payload = " + JSON.stringify(payloadToSend));
      this.apiService.putRequest<any>(endpoint, payloadToSend).subscribe({
        next: () => {
          const items = [...this.service.items];
          const index = items.findIndex((i) => i.id === item.id);
          if (index >= 0) {
            items[index] = item;
            this.service.update(items);
            this.refreshTree();
          }
          this.closeOverlay();
        },
        error: (err) => console.error('Failed to update work item', err),
      });
    }
  }

private refreshTree() {
    const items = this.service.items;
    this.populateDropdownOptions(items); // <-- Refresh dropdown arrays

    const featureNodes = items.filter((i) => i.type === WorkItemType.Feature);
    const storyNodes = items.filter((i) => i.type === WorkItemType.Story);
    const taskNodes = items.filter((i) => i.type === WorkItemType.Task);
    const bugNodes = items.filter((i) => i.type === WorkItemType.Bug);

    this.tree = [...this.buildTreeWithChildren(featureNodes, storyNodes, taskNodes, bugNodes)];
    this.applyFilters();
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
      productCode: item.productCategory,
      sprintCode: item.sprintName,
      userCode: item.assignedTo,
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
      featureCode: item.parentId ?? null,
      sprintCode: item.sprintName,
      userCode: item.assignedTo,
      comments: item.comments,
    };
  }

  toTask(item: WorkItem): ITask {
    return {
      id: item.id,
      title: item.title,
      description: item.description,
      status: item.status,
      priority: item.priority,
      estimatedHours: item.estimatedPoints,
      remainingHours: item.remainingPoints,
      storyCode: item.parentId ?? null,
      sprintCode: item.sprintName,
      userCode: item.assignedTo,
      comments: item.comments,
    };
  }

  toBug(item: WorkItem): IBug {
    return {
      id: Number(item.id.substring(1)),
      bugCode: item.id,
      title: item.title,
      description: item.description,
      bugstatus: item.status,
      priority: item.priority,
      originalestimatehours: item.estimatedPoints,
      remainingestimatehours: item.remainingPoints,
      reopencount: item.reopenCount,
      storyCode: item.parentId ?? null,
      sprintCode: item.sprintName,
      assignedUserCode: item.assignedTo,
      comments: item.comments,
    };
  }

  fromFeature(f: IFeature): WorkItem {
    return {
      id: f.id,
      title: f.title,
      type: WorkItemType.Feature,
      parentId: f.productCode ?? null,
      status: f.status,
      description: f.description,
      sprintName: f.sprintCode,
      priority: f.priority,
      assignedTo: f.userCode,
      productCategory: f.productCode,
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
      parentId: s.featureCode ?? null,
      status: s.status,
      description: s.body,
      sprintName: s.sprintCode,
      priority: s.priority,
      assignedTo: s.userCode,
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
      parentId: t.storyCode ?? null,
      status: t.status,
      description: t.description,
      sprintName: t.sprintCode,
      priority: t.priority,
      assignedTo: t.userCode,
      productCategory: null,
      reopenCount: 0,
      estimatedPoints: t.estimatedHours,
      remainingPoints: t.remainingHours,
      comments: t.comments,
    };
  }

  fromBug(b: IBug): WorkItem {
    return {
      id: b.bugCode,
      title: b.title,
      type: WorkItemType.Bug,
      parentId: b.storyCode ?? null,
      status: b.bugstatus,
      description: b.description,
      sprintName: b.sprintCode,
      priority: b.priority,
      assignedTo: b.assignedUserCode,
      productCategory: null,
      reopenCount: b.reopencount,
      estimatedPoints: b.originalestimatehours,
      remainingPoints: b.remainingestimatehours,
      comments: b.comments != undefined ? b.comments : [],
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
      id: feature.featureCode,
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
      id: 'S' + story.id,
      title: story.title,
      type: WorkItemType.Story,
      parentId: story.featureCode,
      status: story.storyStatus,
      description: story.body,
      sprintName: story.sprintCode,
      priority: story.priority,
      assignedTo: story.userCode,
      productCategory: null,
      reopenCount: 0,
      estimatedPoints: story.storyPoints,
      remainingPoints: story.storyPoints,
      comments: story.comments,
    };
  }

  private mapTask(task: ITasksResponse): WorkItem {
    return {
      id: task.taskCode,
      title: task.title,
      type: WorkItemType.Task,
      parentId: task.storyCode,
      status: task.taskstatus,
      description: task.body,
      sprintName: task.sprintCode,
      priority: task.priority,
      assignedTo: task.userCode,
      productCategory: null,
      reopenCount: 0,
      estimatedPoints: task.originalestimatehours,
      remainingPoints: task.remainingestimatehours,
      comments: task.commentsList,
    };
  }

  private mapBug(bug: IBugResponse): WorkItem {
    return {
      id: bug.bugCode,
      title: bug.title,
      type: WorkItemType.Bug,
      parentId: bug.storyCode,
      status: bug.bugstatus,
      description: bug.description,
      sprintName: bug.sprintCode,
      priority: bug.priority,
      assignedTo: bug.assignedUserCode,
      productCategory: null,
      reopenCount: bug.reopencount,
      estimatedPoints: bug.originalestimatehours,
      remainingPoints: bug.remainingestimatehours,
      comments: bug.comments,
    };
  }

  private buildTreeWithChildren(
    features: WorkItem[],
    stories: WorkItem[],
    tasks: WorkItem[],
    bugs: WorkItem[],
  ): TreeNode[] {
    const map = new Map<string, TreeNode>();

    features.forEach((f) => {
      map.set(this.normalizeId(f.id), {
        ...f,
        children: [],
        expanded: true,
      });
    });

    stories.forEach((s) => {
      const normalizedStoryId = this.normalizeId(s.id);
      const parentId = this.normalizeId(s.parentId);

      const storyNode: TreeNode = {
        ...s,
        children: [],
        expanded: false,
      };
      map.set(normalizedStoryId, storyNode);

      const parentFeature = map.get(parentId);
      if (parentFeature) {
        parentFeature.children.push(storyNode);
      }
    });

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

    return Array.from(map.values()).filter((node) => node.parentId === null);
  }

  private populateDropdownOptions(items: WorkItem[]) {
    this.uniqueProducts = Array.from(new Set(items.map(i => i.productCategory).filter(Boolean))).sort() as string[];
    this.uniqueSprints = Array.from(new Set(items.map(i => i.sprintName).filter(Boolean))).sort() as string[];
    this.uniqueStatuses = Array.from(new Set(items.map(i => i.status).filter(Boolean))).sort() as string[];
    this.uniqueUsers = Array.from(new Set(items.map(i => i.assignedTo).filter(Boolean))).sort() as string[];
  }

  private getBacklog() {
    forkJoin({
      features: this.apiService.getRequest<IFeatureResponse[]>('/backlog/getAllFeatures'),
      storyData: this.apiService.getRequest<any[]>('/story/all'),
      tasks: this.apiService.getRequest<ITasksResponse[]>('/task/all'),
      bugs: this.apiService.getRequest<IBugResponse[]>('/Bug'),
      usersData: this.apiService.getRequest<any[]>('/users/all'),
      featuresData: this.apiService.getRequest<any[]>('/feature'),
      sprintsData: this.apiService.getRequest<any[]>('/sprint/all'),
    }).subscribe({
      next: ({ features, tasks, bugs, usersData, featuresData, storyData, sprintsData }) => {
        const featureNodes = features.map((f) => this.mapFeature(f));
        const storyNodes = storyData.map((s) => this.mapStory(s));
        const taskNodes = tasks.map((t) => this.mapTask(t));
        const bugNodes = bugs.map((b) => this.mapBug(b));

        this.usersList.set(usersData || []);
        this.featuresList.set(featuresData || []);
        this.sprintsList.set(sprintsData || []);
        this.storyList.set(storyData || []);

        const allItems: WorkItem[] = [...featureNodes, ...storyNodes, ...taskNodes, ...bugNodes];
        this.service.update(allItems);

        // Populate dropdown data directly
        this.populateDropdownOptions(allItems);

        // Populate tree configurations
        this.tree = [...this.buildTreeWithChildren(featureNodes, storyNodes, taskNodes, bugNodes)];
        this.applyFilters(); // Initialize display array
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
