package com.busify.project.booking.controller;

import com.busify.project.booking.dto.request.BookingAddRequestDTO;
import com.busify.project.booking.dto.response.BookingAddResponseDTO;
import com.busify.project.booking.service.impl.BookingServiceImpl;
import com.busify.project.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingServiceImpl bookingService;

    @GetMapping
    public ApiResponse<?> getHistoryBookings(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") int page, // Mặc định là 1
            @RequestParam(defaultValue = "10") int size
    ) {
        return bookingService.getBookingHistory(userId, page, size);
    }

    @PostMapping
    public ApiResponse<?> addBooking(@RequestBody BookingAddRequestDTO request) {
        BookingAddResponseDTO response = bookingService.addBooking(request);
        return ApiResponse.success("Thêm đặt vé thành công", response);
    }
}
