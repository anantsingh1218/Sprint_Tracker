import { IComment } from "./storyInterface";
import { WorkStatus } from "./workItem";

export interface ITask {
  id: string;
  title: string;
  description: string;

  storyId: number | null;
  sprintId: number | null;
  userId: number | null;

  status: WorkStatus;
  priority: 'Low' | 'Medium' | 'High';

  estimatedHours: number;
  remainingHours: number;

  comments: IComment[];
}