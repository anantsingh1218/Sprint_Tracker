package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
    Optional<Attachment> findAttachmentByFilename(String originalFileName);
}