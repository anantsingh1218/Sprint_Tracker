package com.sprint.SprintLite.attachment.service.impl;

import com.sprint.SprintLite.attachment.service.IAttachmentService;
import com.sprint.SprintLite.dto.AttachmentRequestDto;
import com.sprint.SprintLite.entity.Attachment;
import com.sprint.SprintLite.entity.AttachmentMapping;
import com.sprint.SprintLite.entity.Users;
import com.sprint.SprintLite.entity.enums.EntityType;
import com.sprint.SprintLite.exception.FileStorageException;
import com.sprint.SprintLite.repository.AttachmentMappingRepository;
import com.sprint.SprintLite.repository.AttachmentRepository;
import com.sprint.SprintLite.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachmentServiceImpl implements IAttachmentService {

    private final UsersRepository usersRepository;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMappingRepository attachmentMappingRepository;

    @Override
    @Transactional
    public AttachmentRequestDto createOrUpdateAttachment(String username, String filename, MultipartFile attachmentFile, Long entityId, EntityType entityType) {
        Users userWhoSentAttachment = usersRepository.findByUsername(username)
                .orElseThrow(() -> {
//                    System.out.println("User NOT found in database");
                    return new RuntimeException("User not found with username: " + username);
                });
        Attachment attachment = new Attachment();
        this.mapToAttachment(attachment, userWhoSentAttachment, attachmentFile, filename);
        attachment = attachmentRepository.save(attachment);
        AttachmentMapping attachmentMapping = mapToAttachmentMapping(attachment, entityType, entityId);
        attachmentMappingRepository.save(attachmentMapping);
        return mapToAttachmentDto(attachment, entityId, entityType);
    }

    private void mapToAttachment(
            Attachment attachment,
            Users uploadedBy,
            MultipartFile file,
            String filename) {

        try {
            if (file == null || file.isEmpty()) {
                throw new FileStorageException("File is empty");
            }

            Path uploadDir = Paths.get("uploads", "attachments");
            Files.createDirectories(uploadDir);

            String safeFileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(filename);

            Path filePath = uploadDir.resolve(safeFileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING);

            attachment.setFilename(safeFileName);
            attachment.setFilepath(filePath.toString());
            attachment.setUploadedby(uploadedBy);

        } catch (IOException e) {
            throw new FileStorageException(
                    "Failed to store file: " + filename +
                            ", reason: " + e.getMessage(),
                    e);
        }
    }

    private AttachmentRequestDto mapToAttachmentDto(Attachment attachment, Long entityId, EntityType entityType){
        return new AttachmentRequestDto(attachment.getFilename(), attachment.getCreatedBy(), attachment.getCreatedAt(), attachment.getUpdatedBy(), attachment.getUpdatedAt(), entityType, entityId);
    }

    private AttachmentMapping mapToAttachmentMapping(Attachment attachment, EntityType entityType, Long entityId){
        AttachmentMapping attachmentMapping = new AttachmentMapping();
        attachmentMapping.setAttachmentid(attachment);
        attachmentMapping.setEntitytype(entityType);
        attachmentMapping.setEntityid(Math.toIntExact(entityId));
        return attachmentMapping;
    }

}
