package com.busify.project.score.service.impl;

import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.score.dto.request.ScoreMinusRequestDTO;
import com.busify.project.score.dto.response.ScoreAddResponseDTO;
import com.busify.project.score.dto.response.ScoreResponseDTO;
import com.busify.project.score.entity.Score;
import com.busify.project.score.entity.ScoreHistory;
import com.busify.project.score.mapper.ScoreMapper;
import com.busify.project.score.repository.ScoreHistoryRepository;
import com.busify.project.score.repository.ScoreRepository;
import com.busify.project.score.service.ScoreService;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {

    private final ScoreRepository scoreRepository;
    private final BookingRepository bookingRepository;
    private final ScoreHistoryRepository scoreHistoryRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtil;

    @Override
    @Transactional
    public List<ScoreAddResponseDTO> addPointsByTripId(Long tripId) {
        List<Bookings> bookings = bookingRepository.findByTripId(tripId);
        List<ScoreAddResponseDTO> responses = new ArrayList<>();

        for (Bookings booking : bookings) {
            User customer = booking.getCustomer();
            if (customer == null) continue;

            // Chỉ xử lý booking đã confirm hoặc completed
            String bookingStatus = String.valueOf(booking.getStatus());
            if (!"confirmed".equalsIgnoreCase(bookingStatus)
                    && !"completed".equalsIgnoreCase(bookingStatus)) {
                continue;
            }

            // Check nếu booking này đã cộng điểm (EARNED) rồi thì bỏ qua
            if (scoreHistoryRepository.existsByBookingAndActionType(booking, "EARNED")) {
                continue;
            }

            // Tính số vé
            int ticketCount = 0;
            if (booking.getSeatNumber() != null && !booking.getSeatNumber().isBlank()) {
                ticketCount = booking.getSeatNumber().split(",").length;
            }
            if (ticketCount == 0) continue;

            // Tìm hoặc tạo Score
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

            // Ghi log lịch sử (EARNED)
            ScoreHistory history = ScoreHistory.builder()
                    .booking(booking)
                    .user(customer)
                    .pointsAdded(ticketCount)
                    .actionType("EARNED")
                    .build();
            scoreHistoryRepository.save(history);

            responses.add(ScoreMapper.toDTO(score));
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public ScoreResponseDTO getScoreByUserId() {
        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Score score = scoreRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Score not found for user id: " + user.getId()));
        return ScoreMapper.toGetScoreByUserDTO(score);
    }

    @Override
    @Transactional
    public ScoreResponseDTO usePoints(ScoreMinusRequestDTO request) {
        System.out.println("==> usePoints request: bookingId="
                + request.getBookingId()
                + ", pointsToUse=" + request.getPointsToUse());

        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Score score = scoreRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Score not found for user id: " + user.getId()));

        int pointsToUse = request.getPointsToUse();
        if (pointsToUse <= 0) {
            throw new IllegalArgumentException("Số điểm phải lớn hơn 0");
        }
        if (score.getPoints() < pointsToUse) {
            throw new IllegalArgumentException("Điểm không đủ để sử dụng");
        }

        // Trừ điểm
        score.setPoints(score.getPoints() - pointsToUse);
        scoreRepository.save(score);

        // Ghi log lịch sử (USED)
        ScoreHistory history = ScoreHistory.builder()
                .booking(bookingRepository.findById(request.getBookingId())
                        .orElseThrow(() -> new EntityNotFoundException("Booking not found")))
                .user(user)
                .pointsAdded(-pointsToUse) // âm
                .actionType("USED")
                .build();
        scoreHistoryRepository.save(history);

        return ScoreMapper.toGetScoreByUserDTO(score);
    }
}
