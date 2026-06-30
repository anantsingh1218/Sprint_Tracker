import { WorkStatus } from "./workItem";

export interface IStoryResponse{
    id: number,
    title: string,
    description: string,
    assignedTo: string,
    featureName: string,
    featureId: number,
    storyStatus: WorkStatus,
    priority: string,
    sprintName: string,
    estimatedStoryPoints: number,
    remainingStoryPoints: number
};