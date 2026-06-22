package com.sprint.SprintLite.entity;

import com.sprint.SprintLite.entity.enums.EntityType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "dsunotes")
public class DSUNote extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;

    @Column(name = "notesdate")
    private LocalDate notesdate;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "entitytype", columnDefinition = "entitytype")
    private EntityType entitytype;

    @Column(name = "entityid")
    private Integer entityid;

}