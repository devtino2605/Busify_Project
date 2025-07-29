package com.busify.project.booking.entity;


import com.busify.project.user.entity.User;
import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.trip.entity.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings", indexes = {
        @Index(name = "idx_booking_customerID_createdAt", columnList = "customer_id, created_at")
})
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bookings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column
    private String guestFullName;

    @Column
    private String guestEmail;

    @Column
    private String guestPhone;

    @Column
    private String guestAddress;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(unique = true, nullable = false)
    private String bookingCode;

    @Column(nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "agent_accept_booking_id")
    private User agentAcceptBooking;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
