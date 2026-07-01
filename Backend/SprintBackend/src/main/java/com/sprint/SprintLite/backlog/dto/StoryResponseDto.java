package com.sprint.SprintLite.backlog.dto;

import com.sprint.SprintLite.entity.enums.Priority;
import com.sprint.SprintLite.entity.enums.Status;

public record StoryResponseDto(
        String storyCode,
        String title,
        String description,
        String featureName,
        Integer featureId,
        String assignedTo,
        Status storyStatus,
        Priority storyPriority,
        String sprintName,
        Integer estimatedStoryPoints,
        Integer remainingStoryPoints
) {
}
