package com.sprint.SprintLite.entity;

import com.sprint.SprintLite.entity.enums.Priority;
import com.sprint.SprintLite.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private Users userid;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "taskstatus", columnDefinition = "status")
    private Status taskstatus;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "priority", columnDefinition = "priority")
    private Priority priority;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "storyid", nullable = false)
    private Story storyid;

    @JsonIgnore
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sprintid", nullable = false)
    private Sprint sprintid;

    @Column(name = "originalestimatehours")
    private Integer originalestimatehours;

    @Column(name = "remainingestimatehours")
    private Integer remainingestimatehours;
    @NonNull
    @OneToMany(mappedBy = "taskid")
    private Set<Worklog> worklogs = new LinkedHashSet<>();

}