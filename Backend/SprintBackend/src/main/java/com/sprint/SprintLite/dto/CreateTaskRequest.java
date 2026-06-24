package com.sprint.SprintLite.dto;

import com.sprint.SprintLite.entity.enums.Priority;
import com.sprint.SprintLite.entity.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaskRequest {

    private String title;

    private String body;

    private String username;

    private Integer sprintid;

    private Integer storyid;

    private Status taskstatus;

    private Priority priority;

    private Integer originalestimatehours;

    private Integer remainingestimatehours;
}