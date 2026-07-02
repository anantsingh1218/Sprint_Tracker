import { Priority, WorkStatus } from "./workItem";

export interface IFeatureResponse {
  id: number;
  title: string;
  description: string;
  featureStatus: WorkStatus;
  priority: Priority;
  sprintName: string;
  remainingStoryPoints: number;
  estimatedStoryPoints: number;
  productName: string;
  assignedTo: string;
}