package com.sprint.SprintLite.entity;

import com.sprint.SprintLite.entity.enums.SprintStatus;
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
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "sprint")
public class Sprint extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

//    @Generated
    @GeneratedColumn(value = "'SP' || id")
    @Column(name = "sprint_code", insertable = false, updatable = false)
    private String sprintCode;

    @Column(name = "sprintname", length = Integer.MAX_VALUE)
    private String sprintName;

    @ColumnDefault("15")
    @Column(name = "sprintduration")
    private Integer sprintDuration;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", columnDefinition = "sprintstatus")
    private SprintStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private Product productid;

    @Column(name = "startdate")
    private Instant startDate;

    @Column(name = "enddate")
    private Instant endDate;
    @NonNull
    @OneToMany(mappedBy = "sprintid")
    private Set<Bug> bugs = new LinkedHashSet<>();
    @NonNull
    @OneToMany(mappedBy = "sprintId")
    private Set<Feature> featuretables = new LinkedHashSet<>();
    @NonNull
    @OneToMany(mappedBy = "sprintid")
    private Set<Story> storytables = new LinkedHashSet<>();
    @NonNull
    @OneToMany(mappedBy = "sprintid")
    private Set<Task> tasktables = new LinkedHashSet<>();

}