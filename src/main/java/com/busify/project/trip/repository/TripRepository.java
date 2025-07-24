package com.busify.project.trip.repository;

import com.busify.project.trip.entity.Trip;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
    @Query("""
    SELECT t
    FROM Trip t
    JOIN t.bus b
    WHERE b.operator.id = :operatorId
      AND t.departureTime > :now
    ORDER BY t.departureTime ASC
    LIMIT 1
""")
    Trip findUpcomingTripsByOperator(@Param("operatorId") Long operatorId,
                                     @Param("now") Instant now);
}
