package com.busify.project.score.service;

import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.score.dto.response.ScoreAddResponseDTO;
import com.busify.project.score.entity.Score;
import com.busify.project.score.entity.ScoreHistory;
import com.busify.project.score.mapper.ScoreMapper;
import com.busify.project.score.repository.ScoreHistoryRepository;
import com.busify.project.score.repository.ScoreRepository;
import com.busify.project.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreService {
    private final ScoreRepository scoreRepository;
    private final BookingRepository bookingRepository;
    private final ScoreHistoryRepository scoreHistoryRepository;

    @Transactional
    public List<ScoreAddResponseDTO> addPointsByTripId(Long tripId) {
        List<Bookings> bookings = bookingRepository.findByTripId(tripId);

        List<ScoreAddResponseDTO> responses = new ArrayList<>();

        for (Bookings booking : bookings) {
            User customer = booking.getCustomer();
            if (customer == null) continue;

            // Chỉ xử lý booking đã confirm
            if (!"confirmed".equalsIgnoreCase(String.valueOf(booking.getStatus()))) {
                continue;
            }

            // Check nếu booking này đã cộng điểm rồi thì bỏ qua
            if (scoreHistoryRepository.existsByBooking(booking)) {
                continue;
            }

            // Tính số vé chỉ dựa vào seatNumber
            int ticketCount = 0;
            if (booking.getSeatNumber() != null && !booking.getSeatNumber().isBlank()) {
                ticketCount = booking.getSeatNumber().split(",").length;
            }

            // Nếu không có vé thì bỏ qua (fix thêm theo yêu cầu trước đó)
            if (ticketCount == 0) {
                continue;
            }

            // Tìm hoặc tạo Score cho user
            Score score = scoreRepository.findByUser(customer)
                    .orElseGet(() -> {
                        Score s = new Score();
                        s.setUser(customer);
                        s.setPoints(0);
                        return s;
                    });

            // Cộng điểm
            score.setPoints(score.getPoints() + ticketCount);
            scoreRepository.save(score);

            // Ghi lại lịch sử
            ScoreHistory history = ScoreHistory.builder()
                    .booking(booking)
                    .user(customer)
                    .pointsAdded(ticketCount)
                    .build();
            scoreHistoryRepository.save(history);

            responses.add(ScoreMapper.toDTO(score));
        }

        return responses;
    }
}