package com.busify.project.bus_operator.entity;

import com.busify.project.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import com.busify.project.bus_operator.enums.OperatorStatus;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "bus_operators")
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
    private User owner;

    @Column(name = "address")
    private String address;

    @Column(name = "hotline", length = 50)
    private String hotline;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(length = 255) // Điều chỉnh độ dài phù hợp
    private String description;

    @Column(name = "licensePath", nullable = false)
    private String licensePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OperatorStatus status;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}