package com.busify.project.trip_seat.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripSeatId implements Serializable {
    @Column(name = "trip_id")
    private Long tripId;

    @Column(name = "seat_number")
    private String seatNumber;

}
