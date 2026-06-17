package com.sprint.SprintLite.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "producttable")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid", nullable = false)
    private Integer id;

    @Column(name = "productname", length = Integer.MAX_VALUE)
    private String productname;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerid")
    private User ownerid;

    @Column(name = "createdat")
    private Instant createdat;

    @Column(name = "createdby", length = Integer.MAX_VALUE)
    private String createdby;

    @Column(name = "updatedat")
    private Instant updatedat;

    @Column(name = "updatedby", length = Integer.MAX_VALUE)
    private String updatedby;


}