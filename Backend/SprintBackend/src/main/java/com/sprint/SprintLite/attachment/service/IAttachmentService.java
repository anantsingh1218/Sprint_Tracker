package com.sprint.SprintLite.attachment.service;

import com.sprint.SprintLite.dto.AttachmentDownloadDto;
import com.sprint.SprintLite.dto.AttachmentRequestDto;
import com.sprint.SprintLite.dto.AttachmentResponseDto;
import com.sprint.SprintLite.entity.enums.EntityType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IAttachmentService {
    List<AttachmentRequestDto> createOrUpdateAttachments(String username, MultipartFile[] attachmentFile, Long entityId, EntityType entityType);
    AttachmentResponseDto getAllAttachments(EntityType entityType, Long entityId, boolean optimizedFunction);
    AttachmentDownloadDto getAttachmentFile(String originalFileName);
}
