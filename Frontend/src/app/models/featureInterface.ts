import { IComment } from "./storyInterface";
import { Priority, WorkStatus } from "./workItem";

export interface IFeature {
  featureCode?: string;
  id?: string;
  title: string;
  description: string;
  featureStatus: WorkStatus;
  priority: Priority;
  productName: string | null;
  sprintName: string | null;
  assignedTo: string | null;
  estimatedStoryPoints: number;
  remainingStoryPoints: number;
  commentsList?: IComment[];
  comments?: string;
}
