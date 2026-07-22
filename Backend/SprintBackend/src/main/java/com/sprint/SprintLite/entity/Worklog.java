package com.sprint.SprintLite.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "worklog")
public class Worklog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskid")
    private Task taskid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bugid")
    private Bug bugid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private Users userid;

    @Column(name = "workdate")
    private LocalDate workdate;

    @Column(name = "hoursspent")
    private BigDecimal hoursspent;

    @Column(name = "remarks", length = Integer.MAX_VALUE)
    private String remarks;

}