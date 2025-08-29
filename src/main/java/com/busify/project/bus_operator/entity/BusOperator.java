package com.busify.project.bus_operator.entity;

import com.busify.project.user.entity.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import com.busify.project.bus_operator.enums.OperatorStatus;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "bus_operators")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusOperator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operator_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "owner_id")
    private Profile owner;

    @Column(name = "address")
    private String address;

    @Column(name = "hotline", length = 50)
    private String hotline;

    @Column(name = "email", nullable = false)
    private String email;

    @Column
    private String description;

    @Column(name = "license_path", nullable = false)
    private String licensePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OperatorStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private boolean isDeleted;

}