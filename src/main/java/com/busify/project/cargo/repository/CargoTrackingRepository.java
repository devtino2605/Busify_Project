package com.busify.project.cargo.repository;

import com.busify.project.cargo.entity.CargoTracking;
import com.busify.project.cargo.enums.CargoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CargoTrackingRepository
 * 
 * Repository interface for CargoTracking entity
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
@Repository
public interface CargoTrackingRepository extends JpaRepository<CargoTracking, Long> {

    /**
     * Find all tracking records for a cargo booking, ordered by created date
     */
    List<CargoTracking> findByCargoBookingCargoBookingIdOrderByCreatedAtDesc(Long cargoBookingId);

    /**
     * Find tracking records by status
     */
    List<CargoTracking> findByStatus(CargoStatus status);

    /**
     * Find tracking records by cargo booking and status
     */
    List<CargoTracking> findByCargoBookingCargoBookingIdAndStatus(Long cargoBookingId, CargoStatus status);

    /**
     * Find latest tracking record for a cargo booking
     */
    @Query("SELECT ct FROM CargoTracking ct WHERE ct.cargoBooking.cargoBookingId = :cargoBookingId " +
            "ORDER BY ct.createdAt DESC LIMIT 1")
    CargoTracking findLatestTrackingByCargoBookingId(@Param("cargoBookingId") Long cargoBookingId);

    /**
     * Find tracking records updated by a specific user
     */
    List<CargoTracking> findByUpdatedById(Long userId);

    /**
     * Find tracking records within a date range
     */
    List<CargoTracking> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    /**
     * Count tracking records for a cargo booking
     */
    long countByCargoBookingCargoBookingId(Long cargoBookingId);
}
