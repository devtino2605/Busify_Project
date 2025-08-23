package com.busify.project.booking.entity;

import com.busify.project.payment.entity.Payment;
import com.busify.project.ticket.entity.Tickets;
import com.busify.project.user.entity.User;
import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.trip.entity.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

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
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(name = "guest_full_name")
    private String guestFullName;

    @Column(name = "guest_email")
    private String guestEmail;

    @Column(name = "guest_phone")
    private String guestPhone;

    @Column(name = "guest_address")
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
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @ManyToOne
    @JoinColumn(name = "agent_accept_booking_id")
    private User agentAcceptBooking;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    @OneToMany(mappedBy = "booking")
    private List<Tickets> tickets;

    @OneToOne(mappedBy = "booking")
    private Payment payment;
}
