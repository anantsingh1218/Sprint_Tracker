import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';

import { WorkItem, WorkItemType } from '../../models/workItem';
import { WorkItemService } from '../../services/workItemService';
import { fadeSlide } from '../../animations/workItemAnimations';
import { FeatureOverlay } from '../feature-overlay/feature-overlay';
import { Story } from '../story/story';
import { IFeature } from '../../models/featureInterface';
import { IStory } from '../../models/storyInterface';

interface TreeNode extends WorkItem {
  children: TreeNode[];
  expanded?: boolean;
}

@Component({
  selector: 'app-backlog',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatMenuModule, FeatureOverlay, Story],
  templateUrl: './backlog.html',
  styleUrls: ['./backlog.css'],
  animations: [fadeSlide],
})
export class Backlog {
  readonly WorkItemType = WorkItemType;

  tree: TreeNode[] = [];

  selectedWorkItem: WorkItem | null = null;
  selectedFeature: IFeature | null = null;
  selectedStory: IStory | null = null;
  selectedEntityItem : IFeature | IStory | null = null;

  parentItem: WorkItem | null = null;
  overlayType: WorkItemType | null = null;
  isOverlayOpen = false;

  constructor(private service: WorkItemService) {
    this.service.items$.subscribe((items) => {
      this.tree = this.buildTree(items);
    });
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
  }

  // ---------- CREATE ----------
  openCreate(type: WorkItemType, parent: WorkItem | null = null) {
    this.overlayType = type;
    this.parentItem = parent;

    const draft = this.service.createEmptyItem(type, parent?.id ?? null);

    this.selectedWorkItem = draft;
    this.selectedEntityItem =
      type === WorkItemType.Feature ? this.toFeature(draft) : this.toStory(draft);
    switch(type){
      case WorkItemType.Feature : 
        this.selectedFeature = this.toFeature(draft);
        break;
      case WorkItemType.Story :
        this.selectedStory = this.toStory(draft);
    }

    this.isOverlayOpen = true;
  }

  // ---------- EDIT ----------
  openEdit(item: WorkItem) {
    this.overlayType = item.type;
    this.parentItem = this.service.items.find((i) => i.id === item.parentId) ?? null;

    const fresh = this.service.items.find((i) => i.id === item.id) ?? item;

    this.selectedWorkItem = fresh;

    this.selectedEntityItem =
      item.type === WorkItemType.Feature ? this.toFeature(fresh) : this.toStory(fresh);
    switch(item.type){
      case WorkItemType.Feature : 
        this.selectedFeature = this.toFeature(fresh);
        break;
      case WorkItemType.Story :
        this.selectedStory = this.toStory(fresh);
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
      description: '',
      status: item.status,
      priority: 'Medium',
      estimatedStoryPoints: 0,
      remainingStoryPoint: 0,
      productId: null,
      sprintId: null,
      userId: null,
      comments: [],
    };
  }

  toStory(item: WorkItem): IStory {
    return {
      id: item.id,
      title: item.title,
      body: '',
      status: item.status,
      priority: 'Medium',
      estimatedStoryPoints: 0,
      remainingStoryPoint: 0,
      featureId: item.parentId ? this.extractNumericId(item.parentId) : null,
      sprintId: null,
      userId: null,
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
    };
  }

  fromStory(s: IStory): WorkItem {
    return {
      id: s.id,
      title: s.title,
      type: WorkItemType.Story,
      parentId: s.featureId ? `F${s.featureId}1` : null,
      status: s.status,
    };
  }
  private resetOverlayState() {
  this.isOverlayOpen = false;
  this.overlayType = null;
  this.parentItem = null;

  this.selectedWorkItem = null;
  this.selectedFeature = null;
  this.selectedStory = null;
  this.selectedEntityItem = null;
}
}
