package com.sprint.SprintLite.entity;

import com.sprint.SprintLite.entity.enums.Priority;
import com.sprint.SprintLite.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "featuretable")
public class Feature extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "featureid", nullable = false)
    private Integer id;

//    @Generated
    @GeneratedColumn(value = "'F' || featureid")
    @Column(name = "feature_code", insertable = false, updatable = false)
    private String featureCode;

    @Column(name = "title", length = Integer.MAX_VALUE)
    private String title;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid", nullable = false)
    private Users userid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private Product productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprintid")
    private Sprint sprintId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "featurestatus", columnDefinition = "status")
    private Status featureStatus;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "priority", columnDefinition = "priority")
    private Priority priority;

    @Column(name = "estimatedstorypoints")
    private Integer estimatedStoryPoints;

    @Column(name = "remainingstorypoints")
    private Integer remainingStoryPoints;
    @NonNull
    @OneToMany(mappedBy = "featureid")
    private Set<Story> storytables = new LinkedHashSet<>();

}