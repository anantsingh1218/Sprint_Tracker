package com.sprint.SprintLite.dto;

import com.sprint.SprintLite.entity.enums.EntityType;

import java.util.List;

public record AttachmentResponseDto(
        List<AttachmentDto> fileToBeFetched,
        Long numberOfFilesFetched,
        EntityType entityType,
        Long entityId
        ) {
}
