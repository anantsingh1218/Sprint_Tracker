package com.sprint.SprintLite.entity;

import com.sprint.SprintLite.entity.enums.EntityType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "entitytype", columnDefinition = "entitytype")
    private EntityType entitytype;

    @Column(name = "entityid")
    private Integer entityid;

}