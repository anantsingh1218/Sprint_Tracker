import { Priority, WorkStatus } from './workItem';

export interface IStory {
  id: string;
  title: string;
  body: string;
  featureCode: string | null;
  sprintCode: string | null;
  userCode: string | null;
  status: WorkStatus;
  priority: Priority;
  estimatedStoryPoints: number;
  remainingStoryPoint: number;
  comments: IComment[];
}

export interface IComment {
  userCode: string;
  text: string;
  createdAt: string;
}
