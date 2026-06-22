package com.sprint.SprintLite.attachment.controller;

import com.sprint.SprintLite.attachment.service.IAttachmentService;
import com.sprint.SprintLite.dto.AttachmentRequestDto;
import com.sprint.SprintLite.entity.enums.EntityType;
import com.sprint.SprintLite.exception.FileStorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attachment")
@RequiredArgsConstructor
public class AttachmentController {

    private final IAttachmentService attachmentService;

    @PutMapping(value = "/upload/{entityType}/{entityId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentRequestDto> createOrUpdateAttachment(
            @RequestPart(value = "attachment")MultipartFile attachment,
            Authentication authentication,
            @PathVariable EntityType entityType, @PathVariable Long entityId) throws FileStorageException{
        String username = "Alice";
        if(authentication != null){
            username = authentication.getName();
        }
        AttachmentRequestDto attachmentRequestDto = attachmentService.createOrUpdateAttachment(username, attachment.getOriginalFilename(), attachment, entityId, entityType);
        return ResponseEntity.ok(attachmentRequestDto);
    }

}
