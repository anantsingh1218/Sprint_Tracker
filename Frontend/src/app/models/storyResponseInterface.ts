import { Priority, WorkStatus } from "./workItem";

export interface IStoryResponse{
    id: number,
    title: string,
    description: string,
    assignedTo: string,
    featureName: string,
    featureCode: string,
    storyStatus: WorkStatus,
    storyPriority: Priority,
    sprintName: string,
    estimatedStoryPoints: number,
    remainingStoryPoints: number
};
