package com.busify.project.trip.repository;

import com.busify.project.trip.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
//    @Query(value = """
//            SELECT t FROM Trip t
//            WHERE t.departureTime BETWEEN :startDate AND :endDate
//            AND t.averageRating >= (SELECT AVG(t2.averageRating) FROM Trip t2)
//            ORDER BY t.averageRating DESC
//            LIMIT 4
//            """)
//    List<Trip> findTopRatedTripsForCurrentWeek(LocalDateTime startDate, LocalDateTime endDate);
}
