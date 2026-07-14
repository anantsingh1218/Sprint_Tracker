package com.sprint.SprintLite.dto;

import com.sprint.SprintLite.entity.enums.Priority;
import com.sprint.SprintLite.entity.enums.Status;

public record CreateFeatureRequest (
    String featureTitle,
    String description,
    String productCategory,
    String sprintName,
    String assignedTo,
    Status featureStatus,
    Priority featurePriority,
    Integer estimatedStoryPoints,
    Integer remainingStoryPoints,
    String comments
){
}
