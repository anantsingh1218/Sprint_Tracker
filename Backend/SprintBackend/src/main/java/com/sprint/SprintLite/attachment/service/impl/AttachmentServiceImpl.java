package com.sprint.SprintLite.attachment.service.impl;

import com.sprint.SprintLite.attachment.service.IAttachmentService;
import com.sprint.SprintLite.dto.AttachmentDownloadDto;
import com.sprint.SprintLite.dto.AttachmentDto;
import com.sprint.SprintLite.dto.AttachmentRequestDto;
import com.sprint.SprintLite.dto.AttachmentResponseDto;
import com.sprint.SprintLite.entity.Attachment;
import com.sprint.SprintLite.entity.AttachmentMapping;
import com.sprint.SprintLite.entity.Users;
import com.sprint.SprintLite.entity.enums.EntityType;
import com.sprint.SprintLite.exception.EntityTypeNotFoundException;
import com.sprint.SprintLite.exception.FileStorageException;
import com.sprint.SprintLite.repository.AttachmentMappingRepository;
import com.sprint.SprintLite.repository.AttachmentRepository;
import com.sprint.SprintLite.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public List<AttachmentRequestDto> createOrUpdateAttachments(
            String username, MultipartFile[] attachmentFiles, Long entityId, EntityType entityType) {

        Users userWhoSentAttachment = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        List<AttachmentRequestDto> resultList = new ArrayList<>();

        for (MultipartFile attachmentFile : attachmentFiles) {
            if (attachmentFile.isEmpty()) continue;

            Attachment attachment = new Attachment();
            this.mapToAttachment(attachment, userWhoSentAttachment, attachmentFile, attachmentFile.getOriginalFilename());
            attachment = attachmentRepository.save(attachment);

            AttachmentMapping attachmentMapping = mapToAttachmentMapping(attachment, entityType, entityId);
            attachmentMappingRepository.save(attachmentMapping);

            resultList.add(mapToAttachmentDto(attachment, entityId, entityType));
        }

        return resultList;
    }

    @Override
    public AttachmentResponseDto getAllAttachments(EntityType entityType, Long entityId, boolean optimizedFunction) {
        if (optimizedFunction){
            return getAllAttachmentsOptimized(entityType, entityId);
        }
        else{
            return getAllAttachmentsNotOptimized(entityType, entityId);
        }
    }

    @Override
    public AttachmentDownloadDto getAttachmentFile(Integer attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new FileStorageException("File not found with attachment Id = " + attachmentId));
        Path path = Paths.get(attachment.getFilepath());
        Resource resource = new FileSystemResource(path);

        String contentType = "application/octet-stream"; // Safe fallback for downloads to handle multiple file types without being specific about only 1 file type

        return new AttachmentDownloadDto(
                resource,
                attachment.getFilename(),
                contentType
        );

    }

    public AttachmentResponseDto getAllAttachmentsNotOptimized(EntityType entityType, Long entityId) {
        List<AttachmentMapping> attachmentMappingList = attachmentMappingRepository.findAttachmentMappingByEntitytypeAndEntityid(entityType, entityId);
        if(attachmentMappingList.isEmpty()){
            throw new EntityTypeNotFoundException(
                    "Entry with entityType = " + entityType.toString() + " and entityId = " + entityId + " was not found"
            );
        }

        List<AttachmentDto> attachmentDtoList = attachmentMappingList.stream()
                .map(AttachmentMapping::getAttachmentid)
                .map(att -> new AttachmentDto(
                        att.getId(), att.getFilename(), att.getFilepath()
                ))
                .toList();
        return new AttachmentResponseDto(
                attachmentDtoList,
                (long) attachmentDtoList.size(),
                entityType,
                entityId
        );
    }

    public AttachmentResponseDto getAllAttachmentsOptimized(EntityType entityType, Long entityId) {
        List<Attachment> attachmentList =
                attachmentMappingRepository.findAttachmentsByEntity(
                        entityType,
                        entityId
                );
        if(attachmentList.isEmpty()){
            throw new EntityTypeNotFoundException(
                    "Entry with entityType = " + entityType.toString() + " and entityId = " + entityId + " was not found"
            );
        }

        List<AttachmentDto> attachmentDtoList = attachmentList.stream()
                .map(attachment -> new AttachmentDto(
                        attachment.getId(),
                        attachment.getFilename(),
                        attachment.getFilepath()
                ))
                .toList();

        return new AttachmentResponseDto(
                attachmentDtoList,
                (long) attachmentDtoList.size(),
                entityType,
                entityId
        );

    }


    private void mapToAttachment(
            Attachment attachment,
            Users uploadedBy,
            MultipartFile file,
            String originalFileName) {

        try {
            if (file == null || file.isEmpty()) {
                throw new FileStorageException("File is empty");
            }

            Path uploadDir = Paths.get("uploads", "attachments");
            Files.createDirectories(uploadDir);

            String uuid = UUID.randomUUID().toString();

            String safeFileName = uuid + "_" + StringUtils.cleanPath(originalFileName);

            Path filePath = uploadDir.resolve(safeFileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING);

            attachment.setFilename(originalFileName);
            attachment.setFilepath(filePath.toString());
            attachment.setUploadedby(uploadedBy);

        } catch (IOException e) {
            throw new FileStorageException(
                    "Failed to store file: " + originalFileName +
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
