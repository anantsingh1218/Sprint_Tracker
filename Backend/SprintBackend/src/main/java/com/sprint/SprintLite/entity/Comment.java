package com.sprint.SprintLite.entity;

import com.sprint.SprintLite.entity.enums.EntityType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "comment", length = Integer.MAX_VALUE)
    private String comment;

    @Column(name = "entitytype", columnDefinition = "entitytype")
    private EntityType entitytype;

    @Column(name = "entityid")
    private Integer entityid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdby")
    private User createdby;

    @Column(name = "createdat")
    private Instant createdat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updatedby")
    private User updatedby;

    @Column(name = "updatedat")
    private Instant updatedat;


}