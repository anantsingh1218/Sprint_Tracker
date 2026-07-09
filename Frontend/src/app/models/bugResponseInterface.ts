import { Priority, WorkStatus } from "./workItem";

/*
public record BugResponseDto(
        Integer id,
        String description,
        Status bugStatus,
        Priority bugPriority,
        String assignedTo,
        Integer storyCode,
        String storyName,
        String sprintName,
        Integer estimatedHours,
        Integer remainingHours,
        Integer reopenCount
) {
}
*/
export interface IBugResponse{
    id:number,
    title: string,
    description: string,
    bugStatus: WorkStatus,
    bugPriority: Priority,
    assignedTo: string,
    storyCode: string,
    storyName: string,
    sprintName: string,
    estimatedHours: number,
    remainingHours: number,
    reopenCount: number
};
