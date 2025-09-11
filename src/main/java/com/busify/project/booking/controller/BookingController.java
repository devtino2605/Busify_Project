package com.busify.project.booking.controller;

import com.busify.project.booking.dto.request.BookingAddRequestDTO;
import com.busify.project.booking.dto.response.BookingAddResponseDTO;
import com.busify.project.booking.dto.response.BookingHistoryResponse;
import com.busify.project.booking.dto.response.BookingStatusCountDTO;
import com.busify.project.booking.dto.response.BookingUpdateResponseDTO;
import com.busify.project.booking.service.impl.BookingServiceImpl;
import com.busify.project.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingServiceImpl bookingService;

    @GetMapping("/{bookingCode}/pdf")
    public ResponseEntity<byte[]> exportBookingToPdf(@PathVariable String bookingCode) {
        byte[] pdfBytes = bookingService.exportBookingToPdf(bookingCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = "ve-xe-" + bookingCode + ".pdf";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

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

    @PostMapping("/manual-booking")
    public ApiResponse<?> addBookingManual(@RequestBody BookingAddRequestDTO request) {
        BookingAddResponseDTO res = bookingService.addBookingManual(request);
        return ApiResponse.success("Thêm đặt vé thành công", res);
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

    @GetMapping("/admin/booking-status-counts")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<BookingStatusCountDTO>> getBookingStatusCounts() {
        List<BookingStatusCountDTO> statusCounts = bookingService.getBookingStatusCounts();
        return ApiResponse.<List<BookingStatusCountDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Booking status counts retrieved successfully")
                .result(statusCounts)
                .build();
    }

    // Admin xem số lượng khách hàng theo trạng thái booking theo năm
    @GetMapping("/admin/booking-status-counts-by-year")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<BookingStatusCountDTO>> getBookingStatusCountsByYear(
            @RequestParam(value = "year", required = false) Integer year) {

        java.time.LocalDate now = java.time.LocalDate.now();
        int reportYear = (year != null) ? year : now.getYear();

        List<BookingStatusCountDTO> statusCounts = bookingService.getBookingStatusCountsByYear(reportYear);
        return ApiResponse.<List<BookingStatusCountDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Booking status counts by year retrieved successfully")
                .result(statusCounts)
                .build();
    }
}
