package com.sprint.SprintLite.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GeneratedColumn;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "producttable")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid", nullable = false)
    private Integer id;

    @GeneratedColumn(value = "'P' || productid")
    @Column(name = "product_code", insertable = false, updatable = false)
    private String productCode;

    @Column(name = "productname")
    private String productname;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerid")
    private Users ownerid;

    @OneToMany(mappedBy = "productId")
    private Set<Feature> featuretables = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productid")
    private Set<UserProductMapping> userproductmappings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productid")
    private Set<Sprint> sprints = new LinkedHashSet<>();
}