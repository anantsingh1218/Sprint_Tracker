package com.sprint.SprintLite.entity;

import com.sprint.SprintLite.entity.enums.Priority;
import com.sprint.SprintLite.entity.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GeneratedColumn;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "storytable")
public class Story extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

//    @Generated
    @GeneratedColumn(value = "'S' || id")
    @Column(name = "story_code", insertable = false, updatable = false)
    private String storyCode;

    @Column(name = "title", length = Integer.MAX_VALUE)
    private String title;

    @Column(name = "body", length = Integer.MAX_VALUE)
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "featureid")
    private Feature featureid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private Users userid;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "storystatus", columnDefinition = "status")
    private Status storystatus;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "priority", columnDefinition = "priority")
    private Priority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprintid")
    private Sprint sprintid;

    @Column(name = "storypoints")
    private Integer storypoints;
    @NonNull
    @OneToMany(mappedBy = "storyid")
    private Set<Bug> bugs = new LinkedHashSet<>();
    @NonNull
    @OneToMany(mappedBy = "storyid")
    private Set<Task> tasktables = new LinkedHashSet<>();


}