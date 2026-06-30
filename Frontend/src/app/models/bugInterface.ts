import { IComment } from './storyInterface';
import { WorkStatus } from './workItem';

export interface IBug {
  id: string;
  title: string;
  description: string;
  storyId: number | null;
  sprintId: number | null;
  assignedTo: number | null;
  status: WorkStatus;
  priority: string;
  estimatedHours: number;
  remainingHours: number;
  reopenCount: number;

  comments: IComment[];
}
