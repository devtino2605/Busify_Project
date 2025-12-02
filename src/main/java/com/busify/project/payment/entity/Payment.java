package com.busify.project.payment.entity;

import com.busify.project.booking.entity.Bookings;
import com.busify.project.cargo.entity.CargoBooking;
import com.busify.project.payment.enums.PaymentMethod;
import com.busify.project.payment.enums.PaymentStatus;
import com.busify.project.refund.entity.Refund;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "payments", indexes = {
        @Index(name = "idx_payment_booking", columnList = "booking_id"),
        @Index(name = "idx_payment_cargo", columnList = "cargo_booking_id"),
        @Index(name = "idx_payment_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    // ===== PAYMENT CAN BE FOR EITHER BOOKING OR CARGO =====
    // One of these must be non-null

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = true)
    private Bookings booking; // For ticket booking payment

    @OneToOne
    @JoinColumn(name = "cargo_booking_id", nullable = true)
    private CargoBooking cargoBooking; // For cargo booking payment

    // ===== PAYMENT DETAILS =====

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(unique = true)
    private String transactionCode;

    @Column(name = "payment_gateway_id")
    private String paymentGatewayId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.pending;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Relationship với Refund
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Refund> refunds;

    // ===== LIFECYCLE CALLBACKS =====

    @PrePersist
    @PreUpdate
    protected void validateAndUpdate() {
        // Validate payment target (must have exactly one: booking XOR cargoBooking)
        validatePaymentTarget();

        // Update timestamp
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Validate that payment has exactly one target (booking or cargoBooking, not
     * both, not none)
     * This ensures data integrity at entity level
     * 
     * @throws IllegalStateException if validation fails
     */
    private void validatePaymentTarget() {
        boolean hasBooking = (booking != null);
        boolean hasCargo = (cargoBooking != null);

        if (!hasBooking && !hasCargo) {
            throw new IllegalStateException(
                    "Payment validation failed: Payment must be associated with either a Booking or a CargoBooking. " +
                            "Both cannot be null.");
        }

        if (hasBooking && hasCargo) {
            throw new IllegalStateException(
                    "Payment validation failed: Payment cannot be associated with both Booking and CargoBooking simultaneously. "
                            +
                            "Only one target is allowed.");
        }
    }

    // ===== HELPER METHODS =====

    /**
     * Check if this is a cargo payment
     */
    public boolean isCargo() {
        return cargoBooking != null;
    }

    /**
     * Check if this is a ticket booking payment
     */
    public boolean isBooking() {
        return booking != null;
    }

    /**
     * Get payment type for display
     */
    public String getPaymentType() {
        if (isBooking())
            return "TICKET";
        if (isCargo())
            return "CARGO";
        return "UNKNOWN";
    }
}
