package com.busify.project.score.repository;

import com.busify.project.booking.entity.Bookings;
import com.busify.project.score.entity.ScoreHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreHistoryRepository extends JpaRepository<ScoreHistory, Long> {
    boolean existsByBooking(Bookings booking);
}
