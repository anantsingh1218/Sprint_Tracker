package com.sprint.SprintLite.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sprint.SprintLite.entity.enums.Priority;
import com.sprint.SprintLite.entity.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
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
@Table(name = "bugs")
public class Bug extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

//    @Generated
    @GeneratedColumn(value = "'B' || id")
    @Column(name = "bug_code", insertable = false, updatable = false)
    private String bugCode;

    @Column(name = "title", length = Integer.MAX_VALUE)
    private String title;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "bugstatus", columnDefinition = "status")
    private Status bugstatus;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "priority", columnDefinition = "priority")
    private Priority priority;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignedto")
    @JsonIgnore
    private Users assignedto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storyid")
    @JsonIgnore
    private Story storyid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprintid")
    @JsonIgnore
    private Sprint sprintid;

    @Column(name = "originalestimatehours")
    private Integer originalestimatehours;

    @Column(name = "remainingestimatehours")
    private Integer remainingestimatehours;

    @ColumnDefault("0")
    @Column(name = "reopencount")
    private Integer reopencount;
    @NonNull
    @OneToMany(mappedBy = "bugid")
    private Set<Worklog> worklogs = new LinkedHashSet<>();

}