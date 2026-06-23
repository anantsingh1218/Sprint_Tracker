package com.sprint.SprintLite.dto;

import com.sprint.SprintLite.entity.enums.EntityType;

import java.time.Instant;

public record AttachmentRequestDto(
    String filename,
    String createdBy,
    Instant createdAt,
    String updatedBy,
    Instant updatedAt,
    EntityType entityType,
    Long entityId
) {
}
