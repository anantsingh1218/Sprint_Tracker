import { IComment } from "./storyInterface";
import { Priority, WorkStatus } from "./workItem";
/*
public class StoryResponseDto {
    private Integer id;
    private String title;
    private String body;
    private String featureCode;
    private String userCode;
    private Status storyStatus;
    private Priority priority;
    private String sprintCode;
    private Integer storyPoints;
    private String comments;
}
*/
export interface IStoryResponse{
    id: number,
    title: string,
    body: string,
    featureCode: string,
    userCode: string,
    storyStatus: WorkStatus,
    priority: Priority,
    sprintCode: string,
    storyPoints: number,
    comments: IComment[]
};
