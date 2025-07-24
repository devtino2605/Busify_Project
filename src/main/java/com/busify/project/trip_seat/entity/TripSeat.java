package com.busify.project.trip_seat.entity;

import java.time.LocalDateTime;

import com.busify.project.user.entity.User;
import com.busify.project.trip_seat.enums.TripSeatStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trip_seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripSeat {
    @EmbeddedId
    private TripSeatId id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripSeatStatus status = TripSeatStatus.AVAILABLE;

    @Column(name = "locked_at")
    private LocalDateTime lockedAt;

    @ManyToOne
    @JoinColumn(name = "locking_user_id")
    private User lockingUser;
}
