package com.busify.project.booking.controller;

import com.busify.project.booking.service.BookingService;
import com.busify.project.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public ApiResponse<?> getHistoryBookings(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") int page, // Mặc định là 1
            @RequestParam(defaultValue = "10") int size
    ) {
        return bookingService.getBookingHistory(userId, page, size);
    }

}
