package com.sprint.SprintLite.attachment.controller;

import com.sprint.SprintLite.attachment.service.IAttachmentService;
import com.sprint.SprintLite.dto.AttachmentDownloadDto;
import com.sprint.SprintLite.dto.AttachmentRequestDto;
import com.sprint.SprintLite.dto.AttachmentResponseDto;
import com.sprint.SprintLite.entity.enums.EntityType;
import com.sprint.SprintLite.exception.FileStorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/attachment")
@RequiredArgsConstructor
public class AttachmentController {

    private final IAttachmentService attachmentService;

    @PutMapping(value = "/upload/{entityType}/{entityId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<AttachmentRequestDto>> createOrUpdateAttachments(
            @RequestPart(value = "attachments") MultipartFile[] attachments, // Changed to array
            Authentication authentication,
            @PathVariable EntityType entityType,
            @PathVariable Long entityId) throws FileStorageException {

        String username = "Alice";
        if (authentication != null) {
            username = authentication.getName();
        }

        List<AttachmentRequestDto> responseDtos = attachmentService.createOrUpdateAttachments(
                username, attachments, entityId, entityType);

        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping(value = "/fetch/{entityType}/{entityId}")
    public ResponseEntity<AttachmentResponseDto> getAllAttachments(
            @PathVariable EntityType entityType,
            @PathVariable Long entityId
    ){
        return ResponseEntity.ok(
                attachmentService.getAllAttachments(entityType, entityId, true)
        );
    }

    @GetMapping("/download/{attachmentId}")
    public ResponseEntity<Resource> downloadAttachment(
            @PathVariable Integer attachmentId,
            @RequestParam(required = false, defaultValue = "false") boolean download
    ){
        AttachmentDownloadDto attachmentDownloadDto = attachmentService.getAttachmentFile(attachmentId);

        String dispositionType = download ? "attachment" : "inline";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        dispositionType + "; filename=\"" + attachmentDownloadDto.filename() + "\"")
                .contentType(MediaType.parseMediaType(attachmentDownloadDto.contentType()))
                .body(attachmentDownloadDto.resource());

    }

}
