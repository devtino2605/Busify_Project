package com.busify.project.audit_log.entity;

import com.busify.project.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String action;

    @Column(name = "target_entity")
    private String targetEntity;

    @Column(name = "target_id")
    private Long targetId;

    @Column(columnDefinition = "JSON")
    private String details;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();
}