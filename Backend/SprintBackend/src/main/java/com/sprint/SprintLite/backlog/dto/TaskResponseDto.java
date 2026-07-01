package com.sprint.SprintLite.backlog.dto;

import com.sprint.SprintLite.entity.enums.Priority;
import com.sprint.SprintLite.entity.enums.Status;

public record TaskResponseDto(
        String taskCode,
        String title,
        String description,
        String storyName,
        Integer storyId,
        String assignedTo,
        Status taskStatus,
        Priority taskPriority,
        String sprintName,
        Integer estimatedHours,
        Integer remainingHours
) {
}
