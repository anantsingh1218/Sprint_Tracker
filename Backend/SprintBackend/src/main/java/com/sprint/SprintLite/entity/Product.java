package com.sprint.SprintLite.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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

    @Column(name = "productname", length = Integer.MAX_VALUE)
    private String productname;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerid")
    private Users ownerid;
    @NonNull
    @OneToMany(mappedBy = "productId")
    private Set<Feature> featuretables = new LinkedHashSet<>();
    @NonNull
    @OneToMany(mappedBy = "productid")
    private Set<UserProductMapping> userproductmappings = new LinkedHashSet<>();

    @NonNull
    @JsonIgnore
    @OneToMany(mappedBy = "productid")
    private Set<Sprint> sprints = new LinkedHashSet<>();

}