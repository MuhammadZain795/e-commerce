package com.projects.e_commerce.domain.user;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import javax.management.relation.Role;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
