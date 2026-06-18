package com.sprint.SprintLite.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "attachments")
public class Attachment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "filename", length = Integer.MAX_VALUE)
    private String filename;

    @Column(name = "filepath", length = Integer.MAX_VALUE)
    private String filepath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploadedby")
    private User uploadedby;

    @Column(name = "createdat")
    private Instant createdat;

    @Column(name = "createdby", length = Integer.MAX_VALUE)
    private String createdby;

    @Column(name = "updatedat")
    private Instant updatedat;

    @Column(name = "updatedby", length = Integer.MAX_VALUE)
    private String updatedby;


}