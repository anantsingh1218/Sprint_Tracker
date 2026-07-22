import { Priority, WorkStatus } from "./workItem";

export interface IFeatureResponse {
  featureCode: string;
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