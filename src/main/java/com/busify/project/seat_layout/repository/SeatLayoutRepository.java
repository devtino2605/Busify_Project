package com.busify.project.seat_layout.repository;

import com.busify.project.seat_layout.entity.SeatLayout;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatLayoutRepository extends JpaRepository<SeatLayout, Integer> {
    @Query("SELECT t.bus.seatLayout FROM Trip t WHERE t.id = :tripId")
    Optional<SeatLayout> findSeatLayoutByTripId(@Param("tripId") Long tripId);
}