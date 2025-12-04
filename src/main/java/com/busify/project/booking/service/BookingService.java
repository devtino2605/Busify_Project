package com.busify.project.booking.service;

import com.busify.project.booking.dto.request.BookingAddRequestDTO;
import com.busify.project.booking.dto.response.*;
import com.busify.project.common.dto.response.ApiResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface BookingService {
    ApiResponse<?> getAllBookings(int page, int size);

    ApiResponse<?> getBookingHistory(int page, int size, String status);

    Map<String, Long> getBookingCountsByStatus();

    ApiResponse<?> getBookingDetail(String bookingCode);

    BookingUpdateResponseDTO updateBooking(String bookingCode, BookingAddRequestDTO request);

    BookingAddResponseDTO addBooking(BookingAddRequestDTO request);

    ApiResponse<?> searchBookings(
            String bookingCode,
            String route,
            String status,
            LocalDate departureDate,
            LocalDate arrivalDate,
            LocalDate startDate,
            LocalDate endDate,
            String sellingMethod, // Added sellingMethod parameter
            int page,
            int size);

    boolean deleteBooking(String bookingCode);

    // Lấy số lượng khách hàng theo trạng thái booking cho biểu đồ tròn
    List<BookingStatusCountDTO> getBookingStatusCounts();

    // Lấy số lượng khách hàng theo trạng thái booking cho biểu đồ tròn - theo năm
    List<BookingStatusCountDTO> getBookingStatusCountsByYear(int year);

    // Tự động cập nhật bookings thành completed khi trip arrived
    int markBookingsAsCompletedWhenTripArrived(Long tripId);

    /**
     * Cancel all active bookings for a trip when trip is cancelled
     * 
     * @param tripId     Trip ID
     * @param reason     Cancellation reason
     * @param autoRefund Whether to automatically process refunds
     * @return Map containing: cancelledCount, refundedCount, totalRefundAmount,
     *         affectedEmails
     */
    Map<String, Object> cancelBookingsWhenTripCancelled(Long tripId, String reason, boolean autoRefund);

    /**
     * Get list of customer emails for a trip (for notification purposes)
     * 
     * @param tripId Trip ID
     * @return List of customer emails
     */
    List<String> getCustomerEmailsByTripId(Long tripId);

    byte[] exportBookingToPdf(String bookingCode);

    List<BookingGuestResponse> getAllGuests();

}
