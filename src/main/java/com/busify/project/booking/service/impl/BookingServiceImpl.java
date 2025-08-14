package com.busify.project.booking.service.impl;

import com.busify.project.booking.dto.request.BookingAddRequestDTO;
import com.busify.project.booking.dto.response.BookingAddResponseDTO;
import com.busify.project.booking.dto.response.BookingDetailResponse;
import com.busify.project.booking.dto.response.BookingHistoryResponse;
import com.busify.project.booking.dto.response.BookingUpdateResponseDTO;
import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.mapper.BookingMapper;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.booking.service.BookingService;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.trip.entity.Trip;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final BookingRepository bookingRepository;
    private final JwtUtils jwtUtil;

    @Override
    public ApiResponse<?> getBookingHistory(int page, int size) {
        // 1. Lấy email user hiện tại từ JWT context
        String email = jwtUtil.getCurrentUserLogin().isPresent() ? jwtUtil.getCurrentUserLogin().get() : "";

        // 2. Lấy user từ DB dựa trên email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 3. Truy vấn booking theo user.id
        Pageable pageable = PageRequest.of(page - 1, size); // page Spring bắt đầu từ 0
        Page<Bookings> bookingPage = bookingRepository.findByCustomerId(user.getId(), pageable);

        // 4. Mapping booking sang DTO
        List<BookingHistoryResponse> content = bookingPage
                .stream()
                .map(BookingMapper::toDTO)
                .collect(Collectors.toList());

        // 5. Đóng gói response
        Map<String, Object> response = new HashMap<>();
        response.put("result", content);
        response.put("pageNumber", bookingPage.getNumber() + 1); // Trả về bắt đầu từ 1
        response.put("pageSize", bookingPage.getSize());
        response.put("totalRecords", bookingPage.getTotalElements());
        response.put("totalPages", bookingPage.getTotalPages());
        response.put("hasNext", bookingPage.hasNext());
        response.put("hasPrevious", bookingPage.hasPrevious());

        return ApiResponse.success("Lấy lịch sử đặt vé thành công", response);
    }

    public BookingAddResponseDTO addBooking(BookingAddRequestDTO request) {
        User customer = null;
        if (request.getCustomerId() != null) {
            customer = userRepository.findById(request.getCustomerId()).orElse(null);
        }
        final Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new IllegalArgumentException("Trip not found with ID: " + request.getTripId()));
        final Bookings result = bookingRepository.save(BookingMapper.fromRequestDTOtoEntity(request, trip, customer,
                request.getGuestFullName(), request.getGuestPhone(), request.getGuestEmail(),
                request.getGuestAddress()));
        return BookingMapper.toResponseAddDTO(result);
    }

    @Override
    public ApiResponse<?> getBookingDetail(String bookingCode) {
        Bookings booking = bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy booking"));

        BookingDetailResponse dto = BookingMapper.toDetailDTO(booking);
        return ApiResponse.success("Lấy chi tiết đặt vé thành công", List.of(dto));
    }

    @Override
    public BookingUpdateResponseDTO updateBooking(String bookingCode, BookingAddRequestDTO request) {
        try {
            // get booking
            Bookings booking = bookingRepository.findByBookingCode(bookingCode)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy booking"));

            // Cập nhật thông tin cho booking
            booking.setGuestFullName(request.getGuestFullName());
            booking.setGuestPhone(request.getGuestPhone());
            booking.setGuestEmail(request.getGuestEmail());
            booking.setGuestAddress(request.getGuestAddress());

            bookingRepository.save(booking);

            return BookingMapper.toUpdateResponseDTO(booking);
        } catch (Exception e) {
            // Log error if needed
            return null;
        }
    }

    @Override
    public List<BookingHistoryResponse> getAllBookings() {
        try {
            List<Bookings> bookings = bookingRepository.findAll();
            return bookings.stream()
                    .map(BookingMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Log error if needed
            return List.of();
        }
    }
}
