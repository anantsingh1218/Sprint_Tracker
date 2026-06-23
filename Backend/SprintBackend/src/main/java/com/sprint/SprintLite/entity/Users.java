package com.sprint.SprintLite.entity;

import com.sprint.SprintLite.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "role", length = Integer.MAX_VALUE)
    private Role role;

    @Column(name = "email", length = Integer.MAX_VALUE)
    private String email;

    @Column(name = "passwordhash", length = Integer.MAX_VALUE)
    private String passwordhash;

}