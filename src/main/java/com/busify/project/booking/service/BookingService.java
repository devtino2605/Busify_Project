package com.busify.project.booking.service;

import com.busify.project.common.dto.response.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface BookingService {
    ApiResponse<?> getBookingHistory(Long userId, int page, int size);

}
