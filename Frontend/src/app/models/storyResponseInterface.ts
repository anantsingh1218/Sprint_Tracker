import { Priority, WorkStatus } from "./workItem";

export interface IStoryResponse{
    id: number,
    title: string,
    description: string,
    assignedTo: string,
    featureName: string,
    featureId: number,
    storyStatus: WorkStatus,
    storyPriority: Priority,
    sprintName: string,
    estimatedStoryPoints: number,
    remainingStoryPoints: number
};