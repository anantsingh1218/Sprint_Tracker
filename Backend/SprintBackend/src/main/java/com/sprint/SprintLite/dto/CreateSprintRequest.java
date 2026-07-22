package com.sprint.SprintLite.dto;

import com.sprint.SprintLite.entity.enums.SprintStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateSprintRequest {

    private String sprintName;
    private String description;

    private String productCode;

    private LocalDate startDate;
    private LocalDate endDate;

    private Long sprintDuration;

    private SprintStatus status;
}