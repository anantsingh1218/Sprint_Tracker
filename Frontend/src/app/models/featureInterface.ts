import { IComment } from "./storyInterface";
import { WorkStatus } from "./workItem";

export interface IFeature {
  id: string;
  title: string;
  description: string;

  productId: number | null;
  sprintId: number | null;
  userId: number | null;

  status: WorkStatus;
  priority: 'Low' | 'Medium' | 'High';

  estimatedStoryPoints: number;
  remainingStoryPoint: number;

  comments: IComment[];
}