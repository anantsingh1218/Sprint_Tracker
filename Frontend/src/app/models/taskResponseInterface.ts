import { Priority, WorkStatus } from "./workItem";

export interface ITasksResponse{
    id: number,
    title: string,
    description: string,
    storyName: string,
    storyCode: string,
    assignedTo: string,
    taskStatus: WorkStatus,
    taskPriority: Priority,
    sprintName: string,
    estimatedHours: number,
    remainingHours: number
};
