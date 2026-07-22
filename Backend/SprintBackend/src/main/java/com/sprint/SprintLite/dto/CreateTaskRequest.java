package com.sprint.SprintLite.dto;

import com.sprint.SprintLite.entity.enums.Priority;
import com.sprint.SprintLite.entity.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaskRequest {

    private String title;

    private String description;

    private String userCode;

    private String sprintCode;

    private String storyCode;

    private Status status;

    private Priority priority;

    private Integer estimatedHours;

    private Integer remainingHours;

    private String comments;
}