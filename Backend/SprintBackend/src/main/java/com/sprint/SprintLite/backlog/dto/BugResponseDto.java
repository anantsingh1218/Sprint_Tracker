package com.sprint.SprintLite.backlog.dto;

import com.sprint.SprintLite.entity.enums.Priority;
import com.sprint.SprintLite.entity.enums.Status;

public record BugResponseDto(
        String bugCode,
        String title,
        String description,
        Status bugStatus,
        Priority bugPriority,
        String assignedTo,
        Integer storyId,
        String storyName,
        String sprintName,
        Integer estimatedHours,
        Integer remainingHours,
        Integer reopenCount
) {
}
