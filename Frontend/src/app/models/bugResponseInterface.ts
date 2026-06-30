import { WorkStatus } from "./workItem";

/*
public record BugResponseDto(
        Integer id,
        String description,
        Status bugStatus,
        Priority bugPriority,
        String assignedTo,
        Integer storyId,
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
    bugPriority: string,
    assignedTo: string,
    storyId: number,
    storyName: string,
    sprintName: string,
    estimatedHours: number,
    remainingHours: number,
    reopenCount: number
};