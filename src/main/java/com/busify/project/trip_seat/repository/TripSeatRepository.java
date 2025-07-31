package com.busify.project.trip_seat.repository;

import com.busify.project.trip_seat.entity.TripSeat;
import com.busify.project.trip_seat.entity.TripSeatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripSeatRepository extends JpaRepository<TripSeat, TripSeatId> {
    @Query("SELECT ts FROM TripSeat ts WHERE ts.id.tripId = :tripId")
    List<TripSeat> findByTripId(Long tripId);
}