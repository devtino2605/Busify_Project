package com.busify.project.trip.repository;

import com.busify.project.trip.dto.response.NextTripsOfOperatorResponseDTO;
import com.busify.project.trip.dto.response.TripDetailResponse;
import com.busify.project.trip.dto.response.TripRouteResponse;
import com.busify.project.trip.dto.response.TripStopResponse;
import com.busify.project.trip.entity.Trip;
import com.busify.project.trip.enums.TripStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                r.route_id AS routeId,
                bo.operator_id AS operatorId,
                bo.name AS operatorName,
                t.departure_time AS departureTime,
                t.estimated_arrival_time AS estimatedArrivalTime,
                r.default_duration_minutes AS estimatedDurationMinutes,
                (SELECT COUNT(*) FROM trip_seats ts WHERE ts.trip_id = t.trip_id AND ts.status = 'available') AS availableSeats,
                b.id AS busId,
                b.total_seats AS busSeats,
                t.price_per_seat AS pricePerSeat,
                AVG(rev.rating) AS averageRating,
                COUNT(DISTINCT rev.review_id) AS totalReviews,

                sl.city AS startCity,
                sl.address AS startAddress,
                sl.longitude AS startLongitude,
                sl.latitude AS startLatitude,

                el.city AS endCity,
                el.address AS endAddress,
                el.longitude AS endLongitude,
                el.latitude AS endLatitude,

                bm.name AS busName,
                b.seat_layout_id AS busLayoutId,
                b.license_plate AS busLicensePlate,
                b.amenities AS busAmenities
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
                bus_models AS bm ON b.model_id = bm.id
            JOIN
                bus_operators AS bo ON b.operator_id = bo.operator_id
            LEFT JOIN
                reviews AS rev ON t.trip_id = rev.trip_id
            WHERE
                t.trip_id = :tripId
            GROUP BY
                t.trip_id,
                r.route_id,
                bo.operator_id,
                bo.name,
                sl.city, sl.address, sl.longitude, sl.latitude,
                el.city, el.address, el.longitude, el.latitude,
                b.id, bm.name, b.seat_layout_id, b.license_plate, b.amenities
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

    @Query(value = """
            SELECT
                t.trip_id AS tripId,
                t.departure_time AS departureTime,
                t.estimated_arrival_time AS estimatedArrivalTime,
                r.default_duration_minutes AS estimatedDurationMinutes,
                (SELECT COUNT(*)
                 FROM trip_seats ts
                 WHERE ts.trip_id = t.trip_id AND ts.status = 'AVAILABLE'
                ) AS availableSeats,
                COUNT(DISTINCT b.id) AS totalSeats,
                b.total_seats AS busSeats,
                b.id AS busId,
                b.license_plate AS busLicensePlate,
                b.status AS busStatus,
                r.route_id AS routeId,
                r.name AS routeName,
                sl.city AS startCity,
                sl.address AS startAddress,
                sl.longitude AS startLongitude,
                sl.latitude AS startLatitude,
                el.city AS endCity,
                el.address AS endAddress,
                el.longitude AS endLongitude,
                el.latitude AS endLatitude,
                t.estimated_arrival_time AS arrivalTime
            FROM
                trips AS t
            JOIN routes AS r ON t.route_id = r.route_id
            JOIN locations AS sl ON r.start_location_id = sl.location_id
            JOIN locations AS el ON r.end_location_id = el.location_id
            JOIN buses AS b ON t.bus_id = b.id
            WHERE
                b.operator_id = :operatorId
                AND t.departure_time > CURRENT_TIMESTAMP
                AND t.status IN ('SCHEDULED', 'ON_TIME', 'DELAYED')
            GROUP BY
                t.trip_id, t.departure_time, t.estimated_arrival_time, 
                r.default_duration_minutes, t.price_per_seat,
                b.id, b.license_plate, b.status, b.total_seats,
                r.route_id, r.name,
                sl.city, sl.address, sl.longitude, sl.latitude,
                el.city, el.address, el.longitude, el.latitude
            ORDER BY
                t.departure_time ASC
            """, nativeQuery = true)
    List<NextTripsOfOperatorResponseDTO> findNextTripsByOperator(@Param("operatorId") Long operatorId);

    @Query("""
                    SELECT t FROM Trip t
                    WHERE (:status IS NULL OR t.status = :status)
                      AND (:keyword IS NULL OR :keyword = '' 
                           OR LOWER(t.route.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            """)
    Page<Trip> searchAndFilterTrips(
            @Param("keyword") String keyword,
            @Param("status") TripStatus status,
            Pageable pageable
    );

    boolean existsByDriverId(Long driverId);
}

