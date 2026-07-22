package com.sprint.SprintLite.dto;

import org.springframework.core.io.Resource;

public record AttachmentDownloadDto(
        Resource resource,
        String filename,
        String contentType
) {
}
