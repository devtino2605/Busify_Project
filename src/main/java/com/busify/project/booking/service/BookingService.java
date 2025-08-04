package com.busify.project.booking.service;

import com.busify.project.booking.dto.response.BookingHistoryResponse;
import com.busify.project.common.dto.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public interface BookingService {
    ApiResponse<?> getBookingHistory(Long userId, int page, int size);
    ApiResponse<?> getBookingDetail(String bookingCode);
}
