package com.busify.project.payment.entity;

import com.busify.project.booking.entity.Bookings;
import com.busify.project.payment.enums.PaymentMethod;
import com.busify.project.payment.enums.PaymentStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Bookings booking;

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
    private Instant paidAt;
}
