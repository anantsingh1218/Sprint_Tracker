package com.sprint.SprintLite.entity;

import com.sprint.SprintLite.entity.enums.SprintStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "sprint")
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "sprintname", length = Integer.MAX_VALUE)
    private String sprintname;

    @ColumnDefault("15")
    @Column(name = "sprintduration")
    private Integer sprintduration;

    @Column(name = "status", columnDefinition = "sprintstatus")
    private SprintStatus status;

    @Column(name = "startdate")
    private LocalDate startdate;

    @Column(name = "enddate")
    private LocalDate enddate;


}