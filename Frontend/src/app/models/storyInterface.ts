import { WorkStatus } from './workItem';

export interface IStory {
  id: string;
  title: string;
  body: string;

  featureId: number | null;
  sprintId: number | null;
  userId: number | null;

  status: WorkStatus;
  priority: 'Low' | 'Medium' | 'High';

  estimatedStoryPoints: number;
  remainingStoryPoint: number;

  comments: IComment[];
}

export interface IComment {
  userId: number;
  text: string;
  createdAt: string;
}
