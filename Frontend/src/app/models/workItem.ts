import { IComment } from "./storyInterface";

export enum WorkItemType {
  Feature = 'Feature',
  Story = 'Story',
  Task = 'Task',
  Bug = 'Bug',
}
export const WORK_STATUSES = {
  OPEN: 'open',
  IN_PROGRESS: 'in-progress',
  BLOCKED: 'blocked',
  CLOSED: 'closed',
  DONE: 'done',
} as const;

export enum Priority {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
  CRITICAL = 'CRITICAL'
};

export enum WorkStatus {
  OPEN = 'OPEN',
  IN_PROGRESS = 'IN_PROGRESS',
  BLOCKED = 'BLOCKED',
  CLOSED = 'CLOSED',
  DONE = 'DONE',
}

export interface WorkItem {
  id: string;
  title: string;
  type: WorkItemType;
  parentId?: string | null;
  status: WorkStatus;
  description: string;
  sprintName: string | null;
  priority: Priority;
  assignedTo: string | null;
  productCategory: string | null;
  reopenCount: number;
  estimatedPoints: number;
  remainingPoints: number;
  comments: IComment[];
}
