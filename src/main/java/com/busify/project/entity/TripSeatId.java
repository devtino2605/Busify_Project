package com.busify.project.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripSeatId implements Serializable {
    @Column(name = "trip_id")
    private Long tripId;

    @Column(name = "seat_number")
    private String seatNumber;

}
