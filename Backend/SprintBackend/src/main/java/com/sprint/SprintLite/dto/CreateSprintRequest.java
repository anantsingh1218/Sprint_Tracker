package com.sprint.SprintLite.dto;

import lombok.Data;

@Data
public class CreateSprintRequest {
    String sprintName;
    private Long sprintDuration;
    private Long productId;
}
