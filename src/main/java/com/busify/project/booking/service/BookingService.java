package com.busify.project.booking.service;

import com.busify.project.common.dto.response.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface BookingService {
    ApiResponse<?> getBookingHistory(int page, int size);
    ApiResponse<?> getBookingDetail(String bookingCode);
}
