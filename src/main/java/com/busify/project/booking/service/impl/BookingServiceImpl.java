package com.busify.project.booking.service.impl;

import com.busify.project.booking.dto.request.BookingAddRequestDTO;
import com.busify.project.booking.dto.response.BookingAddResponseDTO;
import com.busify.project.booking.dto.response.BookingHistoryResponse;
import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.mapper.BookingMapper;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.booking.service.BookingService;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.trip.entity.Trip;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

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

    @Override
    public ApiResponse<?> getBookingHistory(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size); // Vì Spring bắt đầu từ 0
        Page<Bookings> bookingPage = bookingRepository.findByCustomerId(userId, pageable);

        List<BookingHistoryResponse> content = bookingPage
                .stream()
                .map(BookingMapper::toDTO)
                .collect(Collectors.toList());

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
}
