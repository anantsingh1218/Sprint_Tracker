package com.sprint.SprintLite.attachment.service;

import com.sprint.SprintLite.dto.AttachmentRequestDto;
import com.sprint.SprintLite.entity.enums.EntityType;
import org.springframework.web.multipart.MultipartFile;

public interface IAttachmentService {
    AttachmentRequestDto createOrUpdateAttachment(String username, String filename, MultipartFile attachmentFile, Long entityId, EntityType entityType);
}
