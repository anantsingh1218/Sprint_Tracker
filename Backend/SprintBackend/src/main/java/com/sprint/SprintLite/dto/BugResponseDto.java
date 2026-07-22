package com.sprint.SprintLite.dto;

import com.sprint.SprintLite.entity.enums.Priority;
import com.sprint.SprintLite.entity.enums.Status;
import lombok.Data;

import java.util.List;

@Data
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