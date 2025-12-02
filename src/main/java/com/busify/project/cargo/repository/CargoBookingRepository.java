package com.busify.project.cargo.repository;

import com.busify.project.cargo.entity.CargoBooking;
import com.busify.project.cargo.enums.CargoStatus;
import com.busify.project.cargo.enums.CargoType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * CargoBookingRepository
 * 
 * Repository interface for CargoBooking entity
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
@Repository
public interface CargoBookingRepository extends JpaRepository<CargoBooking, Long> {

        /**
         * Find cargo booking by cargo code
         * Uses @EntityGraph to prevent N+1 query problem by eager loading all necessary
         * relationships
         */
        @EntityGraph(attributePaths = { "trip", "trip.route", "trip.route.startLocation", "trip.route.endLocation",
                        "trip.bus", "trip.bus.model", "trip.bus.operator",
                        "pickupLocation", "dropoffLocation", "bookingUser",
                        "images", "trackingHistory", "payment" })
        Optional<CargoBooking> findByCargoCode(String cargoCode);

        /**
         * Find all cargo bookings for a specific trip
         * Uses @EntityGraph to prevent N+1 query problem
         */
        @EntityGraph(attributePaths = { "pickupLocation", "dropoffLocation", "bookingUser", "payment" })
        List<CargoBooking> findByTripId(Long tripId);

        /**
         * Find all cargo bookings for a specific trip with specific status
         */
        List<CargoBooking> findByTripIdAndStatus(Long tripId, CargoStatus status);

        /**
         * Find all cargo bookings for a specific trip excluding cancelled
         */
        List<CargoBooking> findByTripIdAndStatusNot(Long tripId, CargoStatus status);

        /**
         * Find all cargo bookings by user
         * Uses @EntityGraph to prevent N+1 query problem
         */
        @EntityGraph(attributePaths = { "trip", "trip.route", "pickupLocation", "dropoffLocation", "payment" })
        Page<CargoBooking> findByBookingUserId(Long userId, Pageable pageable);

        /**
         * Find all cargo bookings by user and status
         */
        Page<CargoBooking> findByBookingUserIdAndStatus(Long userId, CargoStatus status, Pageable pageable);

        /**
         * Calculate total weight of cargo for a trip (excluding cancelled)
         */
        @Query("SELECT COALESCE(SUM(c.weight), 0) FROM CargoBooking c " +
                        "WHERE c.trip.id = :tripId AND c.status <> 'CANCELLED'")
        BigDecimal calculateTotalCargoWeight(@Param("tripId") Long tripId);

        /**
         * Count cargo bookings by status for a trip
         */
        @Query("SELECT c.status, COUNT(c) FROM CargoBooking c " +
                        "WHERE c.trip.id = :tripId GROUP BY c.status")
        List<Object[]> countCargoByStatusForTrip(@Param("tripId") Long tripId);

        /**
         * Search cargo bookings with complex filters
         */
        @Query("SELECT c FROM CargoBooking c WHERE " +
                        "(:keyword IS NULL OR " +
                        "  LOWER(c.cargoCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "  LOWER(c.senderName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "  LOWER(c.senderPhone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "  LOWER(c.receiverName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "  LOWER(c.receiverPhone) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
                        "(:status IS NULL OR c.status = :status) AND " +
                        "(:cargoType IS NULL OR c.cargoType = :cargoType) AND " +
                        "(:tripId IS NULL OR c.trip.id = :tripId) AND " +
                        "(:pickupLocationId IS NULL OR c.pickupLocation.id = :pickupLocationId) AND " +
                        "(:dropoffLocationId IS NULL OR c.dropoffLocation.id = :dropoffLocationId) AND " +
                        "(:bookingUserId IS NULL OR c.bookingUser.id = :bookingUserId) AND " +
                        "(:createdFrom IS NULL OR c.createdAt >= :createdFrom) AND " +
                        "(:createdTo IS NULL OR c.createdAt <= :createdTo) AND " +
                        "(:minWeight IS NULL OR c.weight >= :minWeight) AND " +
                        "(:maxWeight IS NULL OR c.weight <= :maxWeight)")
        Page<CargoBooking> searchCargo(
                        @Param("keyword") String keyword,
                        @Param("status") CargoStatus status,
                        @Param("cargoType") CargoType cargoType,
                        @Param("tripId") Long tripId,
                        @Param("pickupLocationId") Long pickupLocationId,
                        @Param("dropoffLocationId") Long dropoffLocationId,
                        @Param("bookingUserId") Long bookingUserId,
                        @Param("createdFrom") LocalDateTime createdFrom,
                        @Param("createdTo") LocalDateTime createdTo,
                        @Param("minWeight") BigDecimal minWeight,
                        @Param("maxWeight") BigDecimal maxWeight,
                        Pageable pageable);

        /**
         * Find cargo by sender phone
         */
        List<CargoBooking> findBySenderPhone(String senderPhone);

        /**
         * Find cargo by receiver phone
         */
        List<CargoBooking> findByReceiverPhone(String receiverPhone);

        /**
         * Find pending cargo bookings that need confirmation
         */
        @Query("SELECT c FROM CargoBooking c WHERE c.status = 'PENDING' " +
                        "AND c.trip.departureTime > :now ORDER BY c.createdAt ASC")
        List<CargoBooking> findPendingCargos(@Param("now") LocalDateTime now);

        /**
         * Find cargo bookings by trip and pickup location
         */
        List<CargoBooking> findByTripIdAndPickupLocationId(Long tripId, Long pickupLocationId);

        /**
         * Check if cargo code exists
         */
        boolean existsByCargoCode(String cargoCode);

        /**
         * Find cargo bookings by trip with pagination
         * Uses @EntityGraph to prevent N+1 query problem
         */
        @EntityGraph(attributePaths = { "pickupLocation", "dropoffLocation", "bookingUser", "payment" })
        Page<CargoBooking> findByTripId(Long tripId, Pageable pageable);

        /**
         * Find cargo bookings by status with pagination
         */
        Page<CargoBooking> findByStatus(CargoStatus status, Pageable pageable);

        /**
         * Find active cargo (not cancelled) by trip ID
         * Uses @EntityGraph to prevent N+1 query problem
         */
        @EntityGraph(attributePaths = { "pickupLocation", "dropoffLocation", "bookingUser", "payment" })
        @Query("SELECT c FROM CargoBooking c WHERE c.trip.id = :tripId AND c.status <> 'CANCELLED'")
        List<CargoBooking> findActiveCargoByTripId(@Param("tripId") Long tripId);

        /**
         * Calculate total cargo revenue for a trip (paid only)
         */
        @Query("SELECT COALESCE(SUM(c.totalAmount), 0) FROM CargoBooking c " +
                        "WHERE c.trip.id = :tripId AND c.status = 'DELIVERED'")
        BigDecimal calculateCargoRevenue(@Param("tripId") Long tripId);

        /**
         * Count cargo by status (all trips)
         */
        @Query("SELECT c.status, COUNT(c) FROM CargoBooking c GROUP BY c.status")
        List<Object[]> countCargoByStatus();

        /**
         * Count cargo by status for specific operator
         */
        @Query("SELECT c.status, COUNT(c) FROM CargoBooking c " +
                        "WHERE c.trip.bus.operator.id = :operatorId " +
                        "GROUP BY c.status")
        List<Object[]> countCargoByStatusForOperator(@Param("operatorId") Long operatorId);

        /**
         * Search cargo with all filters (using keyword for flexible search)
         */
        @Query("SELECT c FROM CargoBooking c WHERE " +
                        "(:keyword IS NULL OR LOWER(c.cargoCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        " LOWER(c.senderName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        " LOWER(c.senderPhone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        " LOWER(c.receiverName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        " LOWER(c.receiverPhone) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
                        "(:tripId IS NULL OR c.trip.id = :tripId) AND " +
                        "(:userId IS NULL OR c.bookingUser.id = :userId) AND " +
                        "(:status IS NULL OR c.status = :status) AND " +
                        "(:cargoType IS NULL OR c.cargoType = :cargoType) AND " +
                        "(:senderPhone IS NULL OR c.senderPhone = :senderPhone) AND " +
                        "(:receiverPhone IS NULL OR c.receiverPhone = :receiverPhone) AND " +
                        "(:startDate IS NULL OR c.createdAt >= :startDate) AND " +
                        "(:endDate IS NULL OR c.createdAt <= :endDate)")
        Page<CargoBooking> searchCargo(
                        @Param("keyword") String keyword,
                        @Param("tripId") Long tripId,
                        @Param("userId") Long userId,
                        @Param("status") CargoStatus status,
                        @Param("cargoType") CargoType cargoType,
                        @Param("senderPhone") String senderPhone,
                        @Param("receiverPhone") String receiverPhone,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate,
                        Pageable pageable);

        /**
         * Find expired pending cargo bookings for auto-cancellation
         * Finds cargo bookings that are PENDING and created before the cutoff time
         * and have not been paid (payment is null or pending)
         */
        @Query("SELECT c FROM CargoBooking c WHERE " +
                        "c.createdAt < :cutoffTime AND " +
                        "c.status = 'PENDING' AND " +
                        "(c.payment IS NULL OR c.payment.status = 'pending')")
        List<CargoBooking> findExpiredPendingCargoBookings(@Param("cutoffTime") LocalDateTime cutoffTime);
}
