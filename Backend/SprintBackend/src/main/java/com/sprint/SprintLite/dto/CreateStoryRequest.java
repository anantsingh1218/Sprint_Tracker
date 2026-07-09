package com.sprint.SprintLite.dto;

import com.sprint.SprintLite.entity.enums.Priority;
import com.sprint.SprintLite.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStoryRequest {

    private String title;
    private String body;

    // Business codes (sent from frontend)
    private String featureCode;
    private String sprintCode;
    private String userCode;

    private Status status;
    private Priority priority;
    private Integer storyPoints;

    private String comments;
}