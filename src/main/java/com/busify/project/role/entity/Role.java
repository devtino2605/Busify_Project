package com.busify.project.role.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Simple Role Entity - chỉ cần name, không cần permission phức tạp
 */
@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;
}