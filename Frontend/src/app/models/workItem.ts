export enum WorkItemType {
  Feature = 'Feature',
  Story = 'Story',
  Task = 'Task',
  Bug = 'Bug'
}
export const WORK_STATUSES = {
  TODO: 'todo',
  IN_PROGRESS: 'in-progress',
  DONE: 'done',
  BLOCKED: 'blocked',
  REVIEW: 'review',
  CLOSED: 'closed',
} as const;

export type WorkStatus = typeof WORK_STATUSES[keyof typeof WORK_STATUSES];

export interface WorkItem {
  id: string;
  title: string;
  type: WorkItemType;
  parentId?: string | null;
  status: WorkStatus;
}