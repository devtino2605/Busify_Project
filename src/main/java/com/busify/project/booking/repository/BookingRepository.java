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
}
