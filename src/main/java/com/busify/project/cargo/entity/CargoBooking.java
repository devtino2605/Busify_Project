package com.busify.project.cargo.entity;

import com.busify.project.cargo.enums.CargoStatus;
import com.busify.project.cargo.enums.CargoType;
import com.busify.project.location.entity.Location;
import com.busify.project.payment.entity.Payment;
import com.busify.project.trip.entity.Trip;
import com.busify.project.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * CargoBooking Entity
 * 
 * Represents a cargo/parcel booking on a specific trip
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
@Entity
@Table(name = "cargo_bookings", indexes = {
        @Index(name = "idx_cargo_trip", columnList = "trip_id"),
        @Index(name = "idx_cargo_user", columnList = "booking_user_id"),
        @Index(name = "idx_cargo_code", columnList = "cargo_code"),
        @Index(name = "idx_cargo_status", columnList = "status"),
        @Index(name = "idx_cargo_pickup", columnList = "pickup_location_id"),
        @Index(name = "idx_cargo_dropoff", columnList = "dropoff_location_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CargoBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cargo_booking_id")
    private Long cargoBookingId;

    // ===== RELATIONSHIPS =====

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_user_id")
    private User bookingUser; // Nullable for guest bookings

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_location_id", nullable = false)
    private Location pickupLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dropoff_location_id", nullable = false)
    private Location dropoffLocation;

    // ===== SENDER INFORMATION =====

    @Column(name = "sender_name", nullable = false, length = 100)
    private String senderName;

    @Column(name = "sender_phone", nullable = false, length = 20)
    private String senderPhone;

    @Column(name = "sender_email", length = 100)
    private String senderEmail;

    @Column(name = "sender_address", columnDefinition = "TEXT")
    private String senderAddress;

    // ===== RECEIVER INFORMATION =====

    @Column(name = "receiver_name", nullable = false, length = 100)
    private String receiverName;

    @Column(name = "receiver_phone", nullable = false, length = 20)
    private String receiverPhone;

    @Column(name = "receiver_email", length = 100)
    private String receiverEmail;

    @Column(name = "receiver_address", columnDefinition = "TEXT")
    private String receiverAddress;

    // ===== CARGO INFORMATION =====

    @Column(name = "cargo_code", unique = true, nullable = false, length = 20)
    private String cargoCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "cargo_type", nullable = false)
    private CargoType cargoType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "weight", precision = 10, scale = 2, nullable = false)
    private BigDecimal weight; // in kg

    @Column(name = "dimensions", length = 50)
    private String dimensions; // Format: LxWxH cm

    @Column(name = "declared_value", precision = 12, scale = 2)
    private BigDecimal declaredValue;

    // ===== FEES AND PAYMENT =====

    @Column(name = "cargo_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal cargoFee;

    @Column(name = "insurance_fee", precision = 10, scale = 2)
    @Default
    private BigDecimal insuranceFee = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    // ===== STATUS =====

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Default
    private CargoStatus status = CargoStatus.PENDING;

    // ===== NOTES =====

    @Column(name = "special_instructions", columnDefinition = "TEXT")
    private String specialInstructions; // Special handling instructions

    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes; // Internal notes (not visible to customers)

    // ===== TIMESTAMPS =====

    @Column(name = "created_at", nullable = false)
    @Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "picked_up_at")
    private LocalDateTime pickedUpAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    // ===== PICKUP VERIFICATION =====

    @Column(name = "confirmed_by_staff_id")
    private Long confirmedByStaffId; // Staff who confirmed the pickup

    @Column(name = "pickup_notes", columnDefinition = "TEXT")
    private String pickupNotes; // Notes when cargo is picked up by receiver

    @Column(name = "pickup_token", columnDefinition = "TEXT")
    private String pickupToken; // JWT token for QR code verification

    @Column(name = "token_expires_at")
    private LocalDateTime tokenExpiresAt; // Token expiration time (7 days from arrival)

    // ===== ONE-TO-MANY RELATIONSHIPS =====

    @OneToMany(mappedBy = "cargoBooking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Default
    private List<CargoTracking> trackingHistory = new ArrayList<>();

    @OneToMany(mappedBy = "cargoBooking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Default
    private List<CargoImage> images = new ArrayList<>();

    // ===== ONE-TO-ONE RELATIONSHIP WITH PAYMENT =====
    // Payment được shared giữa Booking và CargoBooking
    // Không cần mappedBy vì Payment có FK cargo_booking_id
    @OneToOne(mappedBy = "cargoBooking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;

    // ===== LIFECYCLE CALLBACKS =====

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ===== HELPER METHODS =====

    /**
     * Calculate total amount based on cargo fee and insurance fee
     */
    public void calculateTotalAmount() {
        this.totalAmount = this.cargoFee.add(this.insuranceFee);
    }

    /**
     * Add tracking record
     */
    public void addTracking(CargoTracking tracking) {
        trackingHistory.add(tracking);
        tracking.setCargoBooking(this);
    }

    /**
     * Add image
     */
    public void addImage(CargoImage image) {
        images.add(image);
        image.setCargoBooking(this);
    }

    /**
     * Check if cargo can be cancelled
     */
    public boolean canBeCancelled() {
        return this.status.canBeCancelled();
    }

    /**
     * Check if cargo is completed
     */
    public boolean isCompleted() {
        return this.status.isCompleted();
    }
}
