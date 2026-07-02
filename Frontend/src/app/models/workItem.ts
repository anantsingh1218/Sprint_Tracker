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
  LOW = 'Low',
  MEDIUM = 'Medium',
  HIGH = 'High',
  CRITICAL = 'Critical'
};

export enum WorkStatus {
  OPEN = 'Open',
  IN_PROGRESS = 'In Progress',
  BLOCKED = 'Blocked',
  CLOSED = 'Closed',
  DONE = 'Done',
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
