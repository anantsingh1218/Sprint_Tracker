package com.sprint.SprintLite.dto;

import com.sprint.SprintLite.entity.enums.Priority;
import com.sprint.SprintLite.entity.enums.Status;
import lombok.Data;

@Data
public class BugDto {
    private String description;
    private String  title;
    private Status bugstatus;
    private Priority priority;

    private Integer assignedto;
    private String sprintCode;
    private String storyCode;

    private Integer originalestimatehours;
    private Integer remainingestimatehours;
    private String comments;
}