import { IComment } from "./storyInterface";
import { Priority, WorkStatus } from "./workItem";

export interface IFeature {
  id: string;
  title: string;
  description: string;
  productCode: string | null;
  sprintCode: string | null;
  userCode: string | null;
  status: WorkStatus;
  priority: Priority;
  estimatedStoryPoints: number;
  remainingStoryPoint: number;
  comments: IComment[];
}
