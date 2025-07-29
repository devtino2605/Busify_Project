package com.busify.project.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.busify.project.booking.entity.Bookings;

public interface BookingsRepository extends JpaRepository<Bookings, Long> {
}
