package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Attachment;
import com.sprint.SprintLite.entity.AttachmentMapping;
import com.sprint.SprintLite.entity.enums.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttachmentMappingRepository extends JpaRepository<AttachmentMapping, Integer> {
    List<AttachmentMapping> findAttachmentMappingByEntitytypeAndEntityid(EntityType entityType, Long entityId);

    @Query("""
        select am.attachmentid
        from AttachmentMapping am
        where am.entitytype = :entityType
          and am.entityid = :entityId
    """)
    List<Attachment> findAttachmentsByEntity(
            @Param("entityType") EntityType entityType,
            @Param("entityId") Long entityId
    );

    List<AttachmentMapping> findAttachmentMappingByAttachmentid(Attachment attachment);

}