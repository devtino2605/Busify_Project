package com.busify.project.booking.repository;

import com.busify.project.booking.dto.response.BookingStatusCountDTO;
import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.enums.BookingStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Bookings, Long> {
    @Query("SELECT b.status, COUNT(b) FROM Bookings b WHERE b.customer.id = :customerId GROUP BY b.status")
    List<Object[]> countBookingsByStatusForCustomer(@Param("customerId") Long customerId);

    @Query("SELECT COUNT(b) FROM Bookings b WHERE b.trip.id = :tripId AND b.status NOT IN (:canceledStatuses)")
    int countBookedSeats(@Param("tripId") Long tripId,
            @Param("canceledStatuses") List<BookingStatus> canceledStatuses);

    Page<Bookings> findByCustomerId(Long customerId, Pageable pageable);

    Page<Bookings> findByCustomerIdAndStatus(Long customerId, BookingStatus status, Pageable pageable);

    Optional<Bookings> findByBookingCode(String bookingCode);

    @Query("SELECT b FROM Bookings b WHERE " +
            "(:bookingCode IS NULL OR b.bookingCode = :bookingCode) AND " +
            "(:status IS NULL OR b.status = :status) AND " +
            "(:routeName IS NULL OR LOWER(b.trip.route.name) LIKE LOWER(CONCAT('%', :routeName, '%'))) AND "
            +
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

    @Query(value = """
            SELECT
                b.status AS status,
                CAST(COUNT(*) AS SIGNED) AS count,
                CAST(ROUND((COUNT(*) * 100.0 / total.total_count), 2) AS DECIMAL(5,2)) AS percentage
            FROM bookings b
            JOIN trips t ON b.trip_id = t.trip_id
            JOIN buses bus ON t.bus_id = bus.id
            CROSS JOIN (
                SELECT COUNT(*) AS total_count
                FROM bookings b2
                JOIN trips t2 ON b2.trip_id = t2.trip_id
                JOIN buses bus2 ON t2.bus_id = bus2.id
                WHERE bus2.operator_id = :operatorId
            ) total
            WHERE bus.operator_id = :operatorId
            GROUP BY b.status, total.total_count
            ORDER BY count DESC
            """, nativeQuery = true)
    List<BookingStatusCountDTO> findBookingStatusCountsByOperator(@Param("operatorId") Long operatorId);

    // Lấy số lượng khách hàng theo trạng thái booking cho biểu đồ tròn
    @Query(value = """
            SELECT
                b.status AS status,
                CAST(COUNT(*) AS SIGNED) AS count,
                CAST(ROUND((COUNT(*) * 100.0 / total.total_count), 2) AS DECIMAL(5,2)) AS percentage
            FROM bookings b
            CROSS JOIN (
                SELECT COUNT(*) AS total_count FROM bookings
            ) total
            GROUP BY b.status, total.total_count
            ORDER BY count DESC
            """, nativeQuery = true)
    List<BookingStatusCountDTO> findBookingStatusCounts();

    // Lấy số lượng khách hàng theo trạng thái booking cho biểu đồ tròn - theo năm
    @Query(value = """
            SELECT
                b.status AS status,
                CAST(COUNT(*) AS SIGNED) AS count,
                CAST(ROUND((COUNT(*) * 100.0 / total.total_count), 2) AS DECIMAL(5,2)) AS percentage
            FROM bookings b
            CROSS JOIN (
                SELECT COUNT(*) AS total_count
                FROM bookings
                WHERE YEAR(created_at) = :year
            ) total
            WHERE YEAR(b.created_at) = :year
            GROUP BY b.status, total.total_count
            ORDER BY count DESC
            """, nativeQuery = true)
    List<BookingStatusCountDTO> findBookingStatusCountsByYear(@Param("year") int year);

    Optional<Bookings> findByBookingCodeAndCustomerId(String bookingCode, Long customerId);

    // Find expired pending bookings for seat release recovery
    @Query("SELECT b FROM Bookings b WHERE " +
            "b.createdAt < :cutoffTime AND " +
            "(b.payment IS NULL OR b.payment.status = 'pending') AND " +
            "b.status NOT IN ('canceled_by_customer', 'canceled_by_operator', 'completed')")
    List<Bookings> findExpiredPendingBookings(@Param("cutoffTime") Instant cutoffTime);

    // Cập nhật status của tất cả bookings thành completed khi trip arrived
    @Modifying
    @Transactional
    @Query("UPDATE Bookings b SET b.status = com.busify.project.booking.enums.BookingStatus.completed WHERE b.trip.id = :tripId AND b.status IN (com.busify.project.booking.enums.BookingStatus.confirmed)")
    int markBookingsAsCompletedByTripId(@Param("tripId") Long tripId);

    List<Bookings> findByTripId(Long tripId);

    // Sum the total revenue from bookings in this month
    @Query("""
            SELECT b
            FROM Bookings b
            WHERE b.status = 'completed'
            AND b.trip.bus.operator.id = :operatorId
            """)
    List<Bookings> findBookingCompeletedByOperator(@Param("operatorId") Long operatorId);
}
