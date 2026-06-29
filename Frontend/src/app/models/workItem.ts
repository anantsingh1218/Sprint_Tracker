export type WorkItemType = 'feature' | 'story' | 'task';
export type WorkStatus = 'todo' | 'in-progress' | 'done';

export interface WorkItem {
  id: string;
  title: string;
  type: WorkItemType;
  parentId?: string | null;
  status: WorkStatus;
}