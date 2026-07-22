import { IComment } from "./storyInterface";
import { Priority, WorkStatus } from "./workItem";

export interface ITask {
  id: string;
  title: string;
  description: string;
  storyCode: string | null;
  sprintCode: string | null;
  userCode: string | null;
  status: WorkStatus;
  priority: Priority;
  estimatedHours: number;
  remainingHours: number;
  comments: IComment[];
}
