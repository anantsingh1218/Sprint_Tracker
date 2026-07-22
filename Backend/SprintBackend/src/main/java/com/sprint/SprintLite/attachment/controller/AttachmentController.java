package com.sprint.SprintLite.attachment.controller;

import com.sprint.SprintLite.attachment.service.IAttachmentService;
import com.sprint.SprintLite.dto.*;
import com.sprint.SprintLite.entity.Attachment;
import com.sprint.SprintLite.entity.AttachmentMapping;
import com.sprint.SprintLite.entity.enums.EntityType;
import com.sprint.SprintLite.exception.FileStorageException;
import com.sprint.SprintLite.repository.AttachmentMappingRepository;
import com.sprint.SprintLite.repository.AttachmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/attachment")
@RequiredArgsConstructor
public class AttachmentController {

    private final IAttachmentService attachmentService;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMappingRepository attachmentMappingRepository;

    @PostMapping(value = "/upload/{entityType}/{entityId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
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

    @GetMapping("/download/{attachmentFileName}")
    public ResponseEntity<Resource> downloadAttachment(
            @PathVariable String attachmentFileName,
            @RequestParam(required = false, defaultValue = "false") boolean download
    ){
        AttachmentDownloadDto attachmentDownloadDto = attachmentService.getAttachmentFile(attachmentFileName);

        String dispositionType = download ? "attachment" : "inline";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        dispositionType + "; filename=\"" + attachmentDownloadDto.filename() + "\"")
                .contentType(MediaType.parseMediaType(attachmentDownloadDto.contentType()))
                .body(attachmentDownloadDto.resource());

    }

    @DeleteMapping(value = "/delete")
    @Transactional
    public ResponseEntity<RegisterResponseDto> deleteAttachment(
            @RequestBody AttachmentDeleteRequestDto attachmentDeleteRequestDto
    ) throws IOException, EntityNotFoundException {
        Attachment attachment = attachmentRepository.findAttachmentByFilename(attachmentDeleteRequestDto.filename())
                .orElseThrow(() -> new FileStorageException("File not found with attachment filename = " + attachmentDeleteRequestDto.filename()));
        String filePath = attachment.getFilepath();
        Path path = Path.of(filePath);
        Files.deleteIfExists(path);
        List<AttachmentMapping> attachmentMappingList = attachmentMappingRepository.findAttachmentMappingByAttachmentid(attachment);
        if(!attachmentMappingList.isEmpty()){
            attachmentMappingRepository.deleteAll(attachmentMappingList);
        }
        else{
            throw new EntityNotFoundException("Attachment mapping for attachment : " + attachment.getFilename() + " was not found");
        }
        attachmentRepository.delete(attachment);
        RegisterResponseDto registerResponseDto = new RegisterResponseDto("File deleted successfully");
        return ResponseEntity.ok(registerResponseDto);
    }

}
