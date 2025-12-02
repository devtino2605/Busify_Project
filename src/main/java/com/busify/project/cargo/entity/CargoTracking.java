package com.busify.project.cargo.entity;

import com.busify.project.cargo.enums.CargoStatus;
import com.busify.project.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * CargoTracking Entity
 * 
 * Represents a tracking/status update record for a cargo booking
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
@Entity
@Table(name = "cargo_tracking", indexes = {
        @Index(name = "idx_tracking_cargo", columnList = "cargo_booking_id"),
        @Index(name = "idx_tracking_status", columnList = "status"),
        @Index(name = "idx_tracking_created", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CargoTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tracking_id")
    private Long trackingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_booking_id", nullable = false)
    private CargoBooking cargoBooking;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CargoStatus status;

    @Column(name = "location", length = 200)
    private String location;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @Column(name = "created_at", nullable = false)
    @Default
    private LocalDateTime createdAt = LocalDateTime.now();

    // For CargoServiceImpl - use trackedAt instead of createdAt
    public LocalDateTime getTrackedAt() {
        return this.createdAt;
    }

    public void setTrackedAt(LocalDateTime trackedAt) {
        this.createdAt = trackedAt;
    }

    // ===== CONSTRUCTOR FOR EASY CREATION =====

    public CargoTracking(CargoBooking cargoBooking, CargoStatus status, String notes, User updatedBy) {
        this.cargoBooking = cargoBooking;
        this.status = status;
        this.notes = notes;
        this.updatedBy = updatedBy;
        this.createdAt = LocalDateTime.now();
    }

    public CargoTracking(CargoBooking cargoBooking, CargoStatus status, String location, String notes, User updatedBy) {
        this.cargoBooking = cargoBooking;
        this.status = status;
        this.location = location;
        this.notes = notes;
        this.updatedBy = updatedBy;
        this.createdAt = LocalDateTime.now();
    }
}
