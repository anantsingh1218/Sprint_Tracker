package com.sprint.SprintLite.entity;

import com.sprint.SprintLite.entity.enums.Priority;
import com.sprint.SprintLite.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tasktable")
public class Task extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title", length = Integer.MAX_VALUE)
    private String title;

    @Column(name = "body", length = Integer.MAX_VALUE)
    private String body;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid", nullable = false)
    private User userid;

    @Column(name = "taskstatus", columnDefinition = "status")
    private Status taskstatus;

    @Column(name = "priority", columnDefinition = "priority")
    private Priority priority;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "storyid", nullable = false)
    private Story storyid;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sprintid", nullable = false)
    private Sprint sprintid;

    @Column(name = "originalestimatehours")
    private Integer originalestimatehours;

    @Column(name = "remainingestimatehours")
    private Integer remainingestimatehours;

    @Column(name = "createdat")
    private Instant createdat;

    @Column(name = "createdby", length = Integer.MAX_VALUE)
    private String createdby;

    @Column(name = "updatedat")
    private Instant updatedat;

    @Column(name = "updatedby", length = Integer.MAX_VALUE)
    private String updatedby;


}