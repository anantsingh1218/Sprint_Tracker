import { Priority, WorkStatus } from './workItem';

export interface IStory {
  id: string;
  title: string;
  body: string;
  featureId: number | null;
  sprintId: string | null;
  userId: string | null;
  status: WorkStatus;
  priority: Priority;
  estimatedStoryPoints: number;
  remainingStoryPoint: number;
  comments: IComment[];
}

export interface IComment {
  userId: number;
  text: string;
  createdAt: string;
}
