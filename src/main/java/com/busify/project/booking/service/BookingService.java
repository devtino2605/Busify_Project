package com.busify.project.booking.service;

import com.busify.project.booking.dto.request.BookingAddRequestDTO;
import com.busify.project.booking.dto.response.BookingAddResponseDTO;
import com.busify.project.booking.dto.response.BookingHistoryResponse;
import com.busify.project.booking.dto.response.BookingStatusCountDTO;
import com.busify.project.booking.dto.response.BookingUpdateResponseDTO;
import com.busify.project.common.dto.response.ApiResponse;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface BookingService {
    List<BookingHistoryResponse> getAllBookings();

    ApiResponse<?> getBookingHistory(int page, int size);

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
            int page,
            int size);

    boolean deleteBooking(String bookingCode);

    // Lấy số lượng khách hàng theo trạng thái booking cho biểu đồ tròn
    List<BookingStatusCountDTO> getBookingStatusCounts();

    // Lấy số lượng khách hàng theo trạng thái booking cho biểu đồ tròn - theo năm
    List<BookingStatusCountDTO> getBookingStatusCountsByYear(int year);

    // Tự động cập nhật bookings thành completed khi trip arrived
    int markBookingsAsCompletedWhenTripArrived(Long tripId);

    byte[] exportBookingToPdf(String bookingCode);

}
