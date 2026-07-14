import { IComment } from "./storyInterface";
import { Priority, WorkStatus } from "./workItem";

/*
public class BugResponseDto {
    private Integer id;
    private String bugCode;
    private String title;
    private String description;
    private String sprintCode;
    private String storyCode;
    private String assignedUserCode;
    private Status bugstatus;
    private Priority priority;
    private Integer originalestimatehours;
    private Integer remainingestimatehours;
    private Integer reopencount;
    private List<CommentDto> comments;
}
*/
export interface IBugResponse{
    id: number,
    bugCode: string,
    title: string,
    description: string,
    sprintCode: string,
    storyCode: string,
    assignedUserCode: string,
    bugstatus: WorkStatus,
    priority: Priority,
    originalestimatehours: number,
    remainingestimatehours: number,
    reopencount: number,
    comments: IComment[]
};
