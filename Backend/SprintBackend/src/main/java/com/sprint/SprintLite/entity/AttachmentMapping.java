package com.sprint.SprintLite.entity;

import com.sprint.SprintLite.entity.enums.EntityType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "attachmentmapping")
public class AttachmentMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachmentid")
    private Attachment attachmentid;

    @Column(name = "entitytype", columnDefinition = "entitytype")
    private EntityType entitytype;

    @Column(name = "entityid")
    private Integer entityid;


}