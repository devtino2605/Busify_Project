package com.busify.project.booking.controller;

import com.busify.project.booking.dto.request.BookingAddRequestDTO;
import com.busify.project.booking.dto.response.BookingAddResponseDTO;
import com.busify.project.booking.dto.response.BookingHistoryResponse;
import com.busify.project.booking.dto.response.BookingUpdateResponseDTO;
import com.busify.project.booking.service.impl.BookingServiceImpl;
import com.busify.project.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingServiceImpl bookingService;

    @GetMapping("/all")
    public ApiResponse<List<BookingHistoryResponse>> getAllBookings() {
        try {
            List<BookingHistoryResponse> bookings = bookingService.getAllBookings();
            return ApiResponse.success("Lấy danh sách đặt vé thành công", bookings);
        } catch (Exception e) {
            return ApiResponse.error(500, "Lỗi khi lấy danh sách đặt vé: " + e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<?> getHistoryBookings(
            @RequestParam(defaultValue = "1") int page, // Mặc định là 1
            @RequestParam(defaultValue = "10") int size) {
        return bookingService.getBookingHistory(page, size);
    }

    @PostMapping
    public ApiResponse<?> addBooking(@RequestBody BookingAddRequestDTO request) {
        BookingAddResponseDTO response = bookingService.addBooking(request);
        return ApiResponse.success("Thêm đặt vé thành công", response);
    }

    @GetMapping("/{bookingCode}")
    public ApiResponse<?> getBookingDetail(@PathVariable String bookingCode) {
        return bookingService.getBookingDetail(bookingCode);
    }

    @PatchMapping("/{bookingCode}")
    public ApiResponse<BookingUpdateResponseDTO> updateBooking(@PathVariable String bookingCode,
            @RequestBody BookingAddRequestDTO request) {
        BookingUpdateResponseDTO response = bookingService.updateBooking(bookingCode, request);
        if (response != null) {
            return ApiResponse.success("Cập nhật đặt vé thành công", response);
        } else {
            return ApiResponse.error(500, "Cập nhật đặt vé thất bại");
        }
    }

    @GetMapping("/search")
    public ApiResponse<?> searchBookings(
            @RequestParam(required = false) String bookingCode,
            @RequestParam(required = false) String route,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate arrivalDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        return bookingService.searchBookings(
                bookingCode, route, status, departureDate, arrivalDate, startDate, endDate, page, size);
    }

    @DeleteMapping("/{bookingCode}")
    public ApiResponse<Boolean> deleteBooking(@PathVariable String bookingCode) {
        try {
            bookingService.deleteBooking(bookingCode);
            return ApiResponse.success("Xóa đặt vé thành công", true);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(404, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalServerError("Lỗi khi xóa đặt vé: " + e.getMessage());
        }
    }

}
