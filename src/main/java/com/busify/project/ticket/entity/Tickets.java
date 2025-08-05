package com.busify.project.ticket.entity;

import com.busify.project.booking.entity.Bookings;
import com.busify.project.ticket.enums.TicketStatus;
import com.busify.project.trip.entity.Trip;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tickets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long ticketId;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Bookings booking;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String passengerName;

    @Column
    private String passengerPhone;

    @Column(unique = true, nullable = false)
    private String ticketCode;

    @Column(nullable = false)
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.valid;
}
