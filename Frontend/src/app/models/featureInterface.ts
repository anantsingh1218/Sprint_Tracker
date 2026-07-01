import { IComment } from "./storyInterface";
import { Priority, WorkStatus } from "./workItem";

export interface IFeature {
  id: string;
  title: string;
  description: string;
  productId: string | null;
  sprintId: string | null;
  userId: string | null;
  status: WorkStatus;
  priority: Priority;
  estimatedStoryPoints: number;
  remainingStoryPoint: number;
  comments: IComment[];
}