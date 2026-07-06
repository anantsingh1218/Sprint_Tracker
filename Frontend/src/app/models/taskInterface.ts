import { IComment } from "./storyInterface";
import { Priority, WorkStatus } from "./workItem";

export interface ITask {
  id: string;
  title: string;
  description: string;
  storyId: number | null;
  sprintId: string | null;
  userId: string | null;
  status: WorkStatus;
  priority: Priority;
  estimatedHours: number;
  remainingHours: number;
  comments: IComment[];
}