package com.sprint.SprintLite.entity;

import com.sprint.SprintLite.entity.enums.Priority;
import com.sprint.SprintLite.entity.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "featuretable")
public class Feature extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "featureid", nullable = false)
    private Integer id;

    @Column(name = "title", length = Integer.MAX_VALUE)
    private String title;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private Product productid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprintid")
    private Sprint sprintid;

    @Column(name = "featurestatus", columnDefinition = "status")
    private Status featurestatus;

    @Column(name = "priority", columnDefinition = "priority")
    private Priority priority;

    @Column(name = "estimatedstorypoints")
    private Integer estimatedstorypoints;

    @Column(name = "remainingstorypoints")
    private Integer remainingstorypoints;

    @Column(name = "createdat")
    private Instant createdat;

    @Column(name = "createdby", length = Integer.MAX_VALUE)
    private String createdby;

    @Column(name = "updatedat")
    private Instant updatedat;

    @Column(name = "updatedby", length = Integer.MAX_VALUE)
    private String updatedby;


}