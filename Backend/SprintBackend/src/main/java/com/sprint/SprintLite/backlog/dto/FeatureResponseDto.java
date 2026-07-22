package com.sprint.SprintLite.backlog.dto;

import com.sprint.SprintLite.dto.CommentDto;
import com.sprint.SprintLite.entity.enums.Status;

import java.util.List;

public record FeatureResponseDto(
        String featureCode,
        String title,
        String description,
        Status featureStatus,
        String priority,
        String sprintName,
        Integer remainingStoryPoints,
        Integer estimatedStoryPoints,
        String productName,
        String assignedTo,
        List<CommentDto> commentsList
) {
}
