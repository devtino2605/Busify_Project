package com.busify.project.booking.service;

import com.busify.project.booking.dto.request.BookingAddRequestDTO;
import com.busify.project.booking.dto.response.BookingHistoryResponse;
import com.busify.project.booking.dto.response.BookingUpdateResponseDTO;
import com.busify.project.common.dto.response.ApiResponse;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface BookingService {
    List<BookingHistoryResponse> getAllBookings();
    ApiResponse<?> getBookingHistory(int page, int size);
    ApiResponse<?> getBookingDetail(String bookingCode);
    BookingUpdateResponseDTO updateBooking(String bookingCode, BookingAddRequestDTO request);
}
