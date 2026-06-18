package com.sprint.SprintLite.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;

@Getter
@Setter
@Entity
@Table(name = "users")

public class Users extends BaseEntity {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", length = Integer.MAX_VALUE)
    private String username;

    @Column(name = "role", length = Integer.MAX_VALUE)
    private String role;

    @Column(name = "email", length = Integer.MAX_VALUE)
    private String email;

    @Column(name = "passwordhash", length = Integer.MAX_VALUE)
    private String passwordhash;

}