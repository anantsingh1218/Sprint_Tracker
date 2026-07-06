package com.sprint.SprintLite.backlog.dto;

import com.sprint.SprintLite.entity.enums.Status;

public record FeatureResponseDto(
        String featureCode,
        String title,
        String description,
        Status featureStatus
       // String priority,
        //String sprintName,
        //Integer remainingStoryPoints,
        //Integer estimatedStoryPoints,
        //String productName,
        //String assignedTo
) {
}
