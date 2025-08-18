package com.busify.project.booking.repository;

import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Bookings, Long> {
    @Query("SELECT COUNT(b) FROM Bookings b WHERE b.trip.id = :tripId AND b.status NOT IN (:canceledStatuses)")
    int countBookedSeats(@Param("tripId") Long tripId, @Param("canceledStatuses") List<BookingStatus> canceledStatuses);

    Page<Bookings> findByCustomerId(Long customerId, Pageable pageable);

    Optional<Bookings> findByBookingCode(String bookingCode);

    @Query("SELECT b FROM Bookings b WHERE " +
            "(:bookingCode IS NULL OR b.bookingCode = :bookingCode) AND " +
            "(:status IS NULL OR b.status = :status) AND " +
            "(:routeName IS NULL OR LOWER(b.trip.route.name) LIKE LOWER(CONCAT('%', :routeName, '%'))) AND " +
            "(:startDate IS NULL OR DATE(b.createdAt) >= :startDate) AND " +
            "(:endDate IS NULL OR DATE(b.createdAt) <= :endDate) AND " +
            "(:departureDate IS NULL OR DATE(b.trip.departureTime) = :departureDate) AND " +
            "(:arrivalDate IS NULL OR DATE(b.trip.estimatedArrivalTime) = :arrivalDate)")
    Page<Bookings> searchBookings(
            @Param("bookingCode") String bookingCode,
            @Param("status") BookingStatus status,
            @Param("routeName") String routeName,
            @Param("startDate") java.time.LocalDate startDate,
            @Param("endDate") java.time.LocalDate endDate,
            @Param("departureDate") java.time.LocalDate departureDate,
            @Param("arrivalDate") java.time.LocalDate arrivalDate,
            Pageable pageable);
}
