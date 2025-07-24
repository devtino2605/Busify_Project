package com.busify.project.ticket.entity;

import com.busify.project.booking.entity.Bookings;
import com.busify.project.ticket.enums.TicketStatus;
import com.busify.project.trip.entity.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tickets")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Tickets {
    @Id
    @Column(name = "trip_id")
    private Long tripId;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.VALID;

    @OneToOne
    @MapsId
    @JoinColumn(name = "trip_id")
    private Trip trip;
}
