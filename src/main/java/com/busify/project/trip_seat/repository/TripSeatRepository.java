package com.busify.project.trip_seat.repository;

import com.busify.project.trip_seat.entity.TripSeat;
import com.busify.project.trip_seat.entity.TripSeatId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripSeatRepository extends JpaRepository<TripSeat, TripSeatId> {
    @Query("SELECT ts FROM TripSeat ts WHERE ts.id.tripId = :tripId")
    List<TripSeat> findByTripId(Long tripId);
    
    TripSeat findByTripIdAndSeatNumber(Long tripId, String seatNumber);

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO trip_seats (seat_number, trip_id, status)
        VALUES (:seatNumber, :tripId, :status)
        ON DUPLICATE KEY UPDATE status = :status
        """, nativeQuery = true)
    void upsertSeat(@Param("tripId") Long tripId,
                    @Param("seatNumber") String seatNumber,
                    @Param("status") String status);
}