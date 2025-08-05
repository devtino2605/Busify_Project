package com.busify.project.trip.repository;

import com.busify.project.trip.dto.response.TripDetailResponse;
import com.busify.project.trip.dto.response.TripRouteResponse;
import com.busify.project.trip.dto.response.TripStopResponse;
import com.busify.project.trip.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
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

    @Query(value = """
            SELECT
                t.trip_id AS id,
                sl.city AS startCity,
                sl.name AS startName,
                sl.address AS startAddress,
                sl.longitude AS startLongitude,
                sl.latitude AS startLatitude,
                el.city AS endCity,
                el.name AS endName,
                el.address AS endAddress,
                el.longitude AS endLongitude,
                el.latitude AS endLatitude,
                r.default_duration_minutes AS estimatedDurationMinutes,
                r.route_id as routeId,
                b.id as busId,
                b.model AS busName,
                b.total_seats AS busSeats,
                b.license_plate AS busLicensePlate,
                b.amenities AS busAmenities,
                bo.operator_id AS operatorId,
                bo.name AS operatorName,
                t.departure_time AS departureTime,
                t.price_per_seat AS pricePerSeat
            FROM
                trips AS t
            JOIN
                routes AS r ON t.route_id = r.route_id
            JOIN
                locations AS sl ON r.start_location_id = sl.location_id
            JOIN
                locations AS el ON r.end_location_id = el.location_id
            JOIN
                buses AS b ON t.bus_id = b.id
            JOIN
                bus_operators AS bo ON b.operator_id = bo.operator_id
            WHERE
                t.trip_id = :tripId
            """, nativeQuery = true)
    TripDetailResponse findTripDetailById(@Param("tripId") Long tripId);

    @Query(value = """
            SELECT
                t.trip_id as tripId,
                bo.name as operatorName,
                sl.name as startLocation,
                el.name as endLocation,
                t.departure_time as departureTime,
                t.estimated_arrival_time as arrivalEstimateTime,
                r.default_duration_minutes as durationMinutes,
                t.price_per_seat as pricePerSeat,
                (SELECT COUNT(*)
                 FROM trip_seats ts
                 WHERE ts.trip_id = t.trip_id AND ts.status = 'AVAILABLE'
                ) as availableSeats,
                (SELECT IFNULL(AVG(rev.rating), 0)
                 FROM reviews rev
                 WHERE rev.trip_id = t.trip_id
                ) as averageRating,
                t.status as status
            FROM
                trips AS t
            JOIN routes AS r ON t.route_id = r.route_id
            JOIN locations AS sl ON r.start_location_id = sl.location_id
            JOIN locations AS el ON r.end_location_id = el.location_id
            JOIN buses AS b ON t.bus_id = b.id
            JOIN bus_operators AS bo ON b.operator_id = bo.operator_id
            WHERE t.route_id = :routeId
                AND t.departure_time > CURRENT_TIMESTAMP
                AND t.status IN ('SCHEDULED', 'ON_TIME', 'DELAYED')
            ORDER BY t.departure_time ASC
            """, nativeQuery = true)
    List<TripRouteResponse> findUpcomingTripsByRoute(@Param("routeId") Long routeId);

    @Query(value = """
            SELECT
                l.city as city,
                l.name as name,
                l.address as address,
                l.longitude as longitude,
                l.latitude as latitude,
                rs.time_offset_from_start as timeOffsetFromStart
            FROM
                route_stops AS rs
            JOIN
                locations AS l ON rs.location_id = l.location_id
            WHERE
                rs.route_id = (SELECT route_id FROM trips WHERE trip_id = :tripId)
            ORDER BY
                rs.stop_order ASC
            """, nativeQuery = true)
    List<TripStopResponse> findTripStopsById(@Param("tripId") Long tripId);
}
