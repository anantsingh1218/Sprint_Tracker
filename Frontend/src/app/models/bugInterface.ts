import { IComment } from './storyInterface';
import { Priority, WorkStatus } from './workItem';

export interface IBug {
  id: string;
  title: string;
  description: string;
  storyId: number | null;
  sprintId: string | null;
  assignedTo: string | null;
  status: WorkStatus;
  priority: Priority;
  estimatedHours: number;
  remainingHours: number;
  reopenCount: number;

  comments: IComment[];
}
