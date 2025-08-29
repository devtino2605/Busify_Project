package com.busify.project.ticket.repository;

import com.busify.project.ticket.entity.Tickets;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Tickets, Long> {
    Optional<Tickets> findByTicketCode(String ticketCode);
    List<Tickets> findByPassengerName(String name);
    List<Tickets> findByPassengerPhone(String phone);

    @Query(value = """
            SELECT 
                t.ticket_id as ticketId,
                t.passenger_name as passengerName,
                t.passenger_phone as passengerPhone,
                COALESCE(u.email, b.guest_email) as email,
                t.seat_number as seatNumber,
                t.status as status,
                t.ticket_code as ticketCode
            FROM tickets t
            INNER JOIN bookings b ON t.booking_id = b.id
            LEFT JOIN profiles p ON b.customer_id = p.id
            LEFT JOIN users u ON p.user_id = u.id
            WHERE b.trip_id = :tripId
            ORDER BY t.seat_number
            """, nativeQuery = true)
    List<Object[]> findPassengersByTripId(@Param("tripId") Long tripId);

    @Query("SELECT t FROM Tickets t JOIN t.booking b WHERE b.trip.id = :tripId AND t.ticketId = :ticketId")
    Optional<Tickets> findByTripIdAndTicketId(@Param("tripId") Long tripId, @Param("ticketId") Long ticketId);

    // delete ticket by ticket code
    @Modifying
    @Transactional
    @Query("DELETE FROM Tickets t WHERE t.ticketCode = :ticketCode")
    void deleteByTicketCode(@Param("ticketCode") String ticketCode);

    // Tìm tất cả tickets theo booking code
    @Query("SELECT t FROM Tickets t JOIN t.booking b WHERE b.bookingCode = :bookingCode")
    List<Tickets> findByBookingCode(@Param("bookingCode") String bookingCode);
}
