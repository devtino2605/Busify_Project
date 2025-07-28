package com.busify.project.seat_layout.repository;

import com.busify.project.seat_layout.entity.SeatLayout;
import jakarta.annotation.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatLayoutRepository extends JpaRepository<SeatLayout, Long> {
}
