package com.busify.project.contract.entity;

import com.busify.project.contract.enums.ContractStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "contracts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String VATCode;

    // Bus operator info
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    // Proposal info
    @Column(nullable = false)
    private Instant startDate;

    @Column(nullable = false)
    private Instant endDate;

    @Column(nullable = false)
    private String operationArea;

    @Column(nullable = false)
    private String licenseUrl;

    // Admin process info
    private Instant approvedDate;

    private String adminNote;

    // Audit
    private Instant lastModified;
    private String lastModifiedBy;

    // Enum: PENDING, APPROVED, REJECTED, NEED_REVISION, ACTIVE, EXPIRED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdDate;

    @Column(nullable = false)
    @UpdateTimestamp
    private Instant updatedDate;
}
