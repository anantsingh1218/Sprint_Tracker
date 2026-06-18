package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
}