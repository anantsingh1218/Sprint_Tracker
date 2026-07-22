import { IComment } from "./storyInterface";
import { Priority, WorkStatus } from "./workItem";
/*
ublic class TaskResponseDto {

    private String taskCode;

    private String title;

    private String body;

    private String userCode;

    private String sprintCode;

    private String storyCode;

    private Status taskstatus;

    private Priority priority;

    private Integer originalestimatehours;

    private Integer remainingestimatehours;

    private List<CommentDto> commentsList;

}

*/
export interface ITasksResponse{
    taskCode: string,
    title: string,
    body: string,
    userCode: string,
    sprintCode: string,
    storyCode: string,
    taskstatus: WorkStatus,
    priority: Priority,
    originalestimatehours: number,
    remainingestimatehours: number
    commentsList: IComment[]
};
