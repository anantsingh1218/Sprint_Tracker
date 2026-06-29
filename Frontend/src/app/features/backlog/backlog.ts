import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { WorkItem } from '../../models/workItem';
import { WorkItemService } from '../../services/workItemService';
import { fadeSlide } from '../../animations/workItemAnimations';

interface TreeNode extends WorkItem {
  children: TreeNode[];
  expanded?: boolean;
}

@Component({
  selector: 'app-backlog',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule
  ],
  templateUrl: './backlog.html',
  styleUrls: ['./backlog.css'],
  animations: [fadeSlide]
})
export class Backlog {

  tree: TreeNode[] = [];

  constructor(private service: WorkItemService) {
    this.service.items$.subscribe(items => {
      this.tree = this.buildTree(items);
    });
  }

  buildTree(items: WorkItem[]): TreeNode[] {
    const map = new Map<string, TreeNode>();

    // create nodes
    items.forEach(i => {
      map.set(i.id, { ...i, children: [], expanded: true });
    });

    const roots: TreeNode[] = [];

    map.forEach(node => {
      if (node.parentId) {
        const parent = map.get(node.parentId);
        if (parent) {
          parent.children.push(node);
        } else {
          roots.push(node);
        }
      } else {
        roots.push(node);
      }
    });

    return roots;
  }

  toggle(node: TreeNode) {
    node.expanded = !node.expanded;
  }
}