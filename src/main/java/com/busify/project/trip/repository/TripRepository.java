package com.busify.project.trip.repository;

import com.busify.project.bus_operator.entity.BusOperator;
import com.busify.project.location.enums.LocationRegion;
import com.busify.project.trip.dto.response.*;
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
                t.departure_time + INTERVAL 7 HOUR AS departureTime,
                t.estimated_arrival_time + INTERVAL 7 HOUR AS estimatedArrivalTime,
                r.default_duration_minutes AS estimatedDurationMinutes,
                (SELECT COUNT(*) FROM trip_seats ts WHERE ts.trip_id = t.trip_id AND ts.status = 'available') AS availableSeats,
                b.id AS busId,
                b.total_seats AS busSeats,
                t.price_per_seat AS pricePerSeat,
                t.price_per_seat AS originalPrice,
                0 AS discountAmount,
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
                b.amenities AS busAmenities,

                d.id AS driverId,
                p.full_name AS driverName
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
                employees AS d ON t.driver_id = d.id
            LEFT JOIN
                profiles AS p ON d.id = p.id
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
                b.id, bm.name, b.seat_layout_id, b.license_plate, b.amenities,
                d.id, p.full_name
            """, nativeQuery = true)
    TripDetailResponse findTripDetailById(@Param("tripId") Long tripId);

    @Query(value = """
            SELECT
                bi.id AS id,
                bi.image_url AS imageUrl,
                bi.is_primary AS isPrimary
            FROM bus_images bi
            WHERE bi.bus_id = :busId
            """, nativeQuery = true)
    List<BusImageResponse> findBusImagesByBusId(@Param("busId") Long busId);

    @Query(value = """
            SELECT
                t.trip_id as tripId,
                bo.name as operatorName,
                bo.avatar as operatorAvatar,
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
                AND t.status IN ('SCHEDULED', 'ON_SELL', 'DELAYED')
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
                t.estimated_arrival_time AS arrivalTime,
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
                AND t.status IN ('SCHEDULED', 'ON_SELL', 'DELAYED')
            GROUP BY
                t.trip_id, t.departure_time, t.estimated_arrival_time,
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
                JOIN FETCH t.bus b
                WHERE (:operatorName IS NULL OR
                  LOWER(t.bus.operator.name) LIKE LOWER(CONCAT('%', :operatorName, '%')))
                  AND (:untilTime IS NULL OR t.estimatedArrivalTime < :untilTime)
                  AND (:departureDate IS NULL OR t.departureTime >= :departureDate)
                  AND (:startLocation IS NULL OR t.route.startLocation.id = :startLocation)
                  AND (:endLocation IS NULL OR t.route.endLocation.id = :endLocation)
                  AND (:status IS NULL OR t.status = :status)
                  AND (:availableSeats IS NULL OR (SELECT COUNT(ts) FROM TripSeat ts WHERE ts.id.tripId = t.id AND ts.status = 'available') >= :availableSeats)
            """)
    Page<Trip> filterTrips(
            @Param("operatorName") String operatorName,
            @Param("untilTime") Instant untilTime,
            @Param("departureDate") Instant departureDate,
            @Param("startLocation") Long startLocation,
            @Param("endLocation") Long endLocation,
            @Param("status") TripStatus status,
            @Param("availableSeats") Integer availableSeats,
            Pageable pageable);

    @Query("""
                SELECT t FROM Trip t
                JOIN FETCH t.bus b
                WHERE (:departureDate IS NULL OR t.departureTime >= :departureDate)
                  AND (:untilTime IS NULL OR t.estimatedArrivalTime < :untilTime)
                  AND (:startLocation IS NULL OR t.route.startLocation.id = :startLocation)
                  AND (:endLocation IS NULL OR t.route.endLocation.id = :endLocation)
                  AND (:status IS NULL OR t.status = :status)
                  AND (:availableSeats IS NULL OR (SELECT COUNT(ts) FROM TripSeat ts WHERE ts.id.tripId = t.id AND ts.status = 'available') >= :availableSeats)
                ORDER BY t.departureTime ASC
            """)
    List<Trip> searchTrips(
            @Param("departureDate") Instant departureDate,
            @Param("untilTime") Instant untilTime,
            @Param("startLocation") Long startLocation,
            @Param("endLocation") Long endLocation,
            @Param("status") TripStatus status,
            @Param("availableSeats") Integer availableSeats);

    @Query("""

                SELECT t FROM Trip t
                JOIN t.bus b
                WHERE (:status IS NULL OR t.status = :status)
                  AND (:keyword IS NULL OR :keyword = ''
                       OR LOWER(t.route.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                       OR LOWER(t.bus.licensePlate) LIKE LOWER(CONCAT('%', :keyword, '%'))
                       OR LOWER(t.driver.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                      )
                  AND (:operatorId IS NULL OR b.operator.id = :operatorId)
                ORDER BY t.departureTime DESC
            """)
    Page<Trip> searchAndFilterTrips(
            @Param("keyword") String keyword,
            @Param("status") TripStatus status,
            @Param("operatorId") Long operatorId,
            Pageable pageable);

    boolean existsByDriverId(Long driverId);

    boolean existsByBusId(Long busId);

    @Query(value = """
            SELECT new com.busify.project.trip.dto.response.ReportTripResponseDTO(
                   t.id AS tripId,
                   t.departureTime AS departureTime,
                   t.estimatedArrivalTime AS arrivalTime,
                   sl.address AS startLocation,
                   el.address AS endLocation,
                   (SELECT COUNT(bkg) FROM Bookings bkg WHERE bkg.trip.id = t.id) as totalPassengers,
                  COALESCE(SUM(p.amount), 0) AS totalIncome,
                   b.id AS busId
                   )
                   FROM Trip t
                   JOIN t.route r
                   JOIN r.startLocation sl
                   JOIN r.endLocation el
                   JOIN t.bus b
                   JOIN Bookings bo ON bo.trip = t
                   JOIN bo.payment p
                   WHERE b.operator.id = :operatorId
                   AND t.estimatedArrivalTime < CURRENT_TIMESTAMP
                   GROUP BY
                   t.id, t.departureTime, t.estimatedArrivalTime, sl.address, el.address, b.id
            """)
    List<ReportTripResponseDTO> findReportTripByOperatorId(@Param("operatorId") Long operatorId);

    // Query để lấy top 10 trips có doanh thu cao nhất theo năm
    @Query(value = """
            SELECT
                t.trip_id as tripId,
                CONCAT(sl.name, ' → ', el.name) as routeName,
                sl.name as startLocation,
                el.name as endLocation,
                t.departure_time as departureTime,
                CONCAT(bus.model, ' (', bus.license_plate, ')') as busName,
                bo.name as busOperatorName,
                COUNT(booking.id) as totalBookings,
                COALESCE(SUM(booking.total_amount), 0) as totalRevenue,
                t.price_per_seat as pricePerSeat
            FROM trips t
            LEFT JOIN routes r ON t.route_id = r.route_id
            LEFT JOIN locations sl ON r.start_location_id = sl.location_id
            LEFT JOIN locations el ON r.end_location_id = el.location_id
            LEFT JOIN buses bus ON t.bus_id = bus.id
            LEFT JOIN bus_operators bo ON bus.operator_id = bo.operator_id
            LEFT JOIN bookings booking ON t.trip_id = booking.trip_id
                AND booking.status IN ('confirmed', 'completed')
                AND YEAR(booking.created_at) = :year
            GROUP BY t.trip_id, sl.name, el.name, t.departure_time, bus.model, bus.license_plate, bo.name, t.price_per_seat
            HAVING COUNT(booking.id) > 0
            ORDER BY totalRevenue DESC
            LIMIT 10
            """, nativeQuery = true)
    List<TopTripRevenueDTO> findTop10TripsByRevenueAndYear(@Param("year") Integer year);

    @Query(value = """
            SELECT
                t.trip_id,
                t.departure_time,
                t.estimated_arrival_time,
                t.status,
                t.price_per_seat,
                bo.name AS operator_name,
                r.route_id,
                sl.city AS start_city,
                sl.address AS start_address,
                el.city AS end_city,
                el.address AS end_address,
                b.license_plate AS bus_license_plate,
                b.model AS bus_model,
                (SELECT COUNT(*)
                 FROM trip_seats ts
                 WHERE ts.trip_id = t.trip_id AND ts.status = 'AVAILABLE'
                ) AS available_seats,
                b.total_seats,
                (SELECT IFNULL(AVG(rev.rating), 0)
                 FROM reviews rev
                 WHERE rev.trip_id = t.trip_id
                ) AS average_rating
            FROM
                trips AS t
            JOIN routes AS r ON t.route_id = r.route_id
            JOIN locations AS sl ON r.start_location_id = sl.location_id
            JOIN locations AS el ON r.end_location_id = el.location_id
            JOIN buses AS b ON t.bus_id = b.id
            JOIN bus_operators AS bo ON b.operator_id = bo.operator_id
            WHERE
                t.driver_id = :driverId
            ORDER BY
                t.departure_time DESC
            """, nativeQuery = true)
    List<Object[]> findTripsByDriverId(@Param("driverId") Long driverId);

    @Query("""
                SELECT CASE WHEN COUNT(t.id) > 0 THEN TRUE ELSE FALSE END
                FROM Trip t
                JOIN t.bookings b
                JOIN b.customer c
                WHERE t.id = :tripId
                  AND c.email = :email
                  AND b.status = 'completed'
                  AND t.status = 'arrived'
            """)
    Boolean isUserCanReviewTrip(@Param("tripId") Long id, @Param("email") String email);

    @Query("""
                SELECT t FROM Trip t
                WHERE t.driver.id = :driverId
                  AND t.id <> :excludeTripId
                  AND (
                        (t.departureTime <= :newArrivalTime AND t.estimatedArrivalTime >= :newDepartureTime)
                      )
            """)
    List<Trip> findOverlappingTripsForDriver(
            @Param("driverId") Long driverId,
            @Param("newDepartureTime") Instant newDepartureTime,
            @Param("newArrivalTime") Instant newArrivalTime,
            @Param("excludeTripId") Long excludeTripId);

    @Query("SELECT t FROM Trip t " +
            "WHERE t.bus.id = :busId " +
            "AND t.id <> :excludeTripId " +
            "AND (t.departureTime <= :newArrival AND t.estimatedArrivalTime >= :newDeparture)")
    List<Trip> findOverlappingTripsForBus(
            @Param("busId") Long busId,
            @Param("newDeparture") Instant newDeparture,
            @Param("newArrival") Instant newArrival,
            @Param("excludeTripId") Long excludeTripId);

    @Query("SELECT b.operator FROM Trip t JOIN t.bus b WHERE t.id = :tripId")
    BusOperator findOperatorByTripId(@Param("tripId") Long tripId);

    // Query để lấy chuyến đi sắp khởi hành của tài xế (chỉ những chuyến đi trong
    // tương lai)
    @Query("""
            SELECT t FROM Trip t
            WHERE t.driver.id = :driverId
              AND t.departureTime > :currentTime
            ORDER BY t.departureTime ASC
            """)
    List<Trip> findUpcomingTripsByDriverId(@Param("driverId") Long driverId, @Param("currentTime") Instant currentTime);

    @Query("""
             SELECT
                t.id as tripId,
                bo.name as operatorName,
                sl.name as startLocation,
                el.name as endLocation,
                t.departureTime as departureTime,
                t.estimatedArrivalTime as arrivalEstimateTime,
                r.defaultDurationMinutes as durationMinutes,
                t.pricePerSeat as pricePerSeat,
                (SELECT COUNT(*)
                 FROM TripSeat ts
                 WHERE ts.id.tripId = t.id AND ts.status = 'AVAILABLE'
                ) as availableSeats,
                (SELECT IFNULL(AVG(rev.rating), 0)
                 FROM Review rev
                 WHERE rev.trip.id = t.id
                ) as averageRating,
                t.status as status
            FROM
                Trip AS t
            JOIN Route AS r ON t.route.id = r.id
            JOIN Location AS sl ON r.startLocation.id = sl.id
            JOIN Location AS el ON r.endLocation.id = el.id
            JOIN Bus AS b ON t.bus.id = b.id
            JOIN BusOperator AS bo ON b.operator.id = bo.id
            WHERE t.departureTime > CURRENT_TIMESTAMP
                AND t.status = 'ON_SELL'
                AND sl.region = :region
            ORDER BY t.departureTime ASC
            LIMIT 4
            """)
    List<TripRouteResponse> findTripsByRegion(@Param("region") LocationRegion region);

    // Count the number of trips in this month
    @Query("""
            SELECT t
            FROM Trip t
            WHERE t.status = 'arrived'
            AND t.bus.operator.id = :operatorId
            """)
    List<Trip> findTripArrivedByOperatorId(@Param("operatorId") Long operatorId);

    @Query(value = """
            SELECT
                t.*
            FROM
                trips AS t
            JOIN routes AS r ON t.route_id = r.route_id
            JOIN locations AS sl ON r.start_location_id = sl.location_id
            JOIN locations AS el ON r.end_location_id = el.location_id
            JOIN buses AS b ON t.bus_id = b.id
            JOIN bus_operators AS bo ON b.operator_id = bo.operator_id
            WHERE t.route_id = :routeId
                AND t.departure_time > CURRENT_TIMESTAMP
                AND t.status IN ('SCHEDULED', 'ON_SELL', 'DELAYED')
                AND t.trip_id != :excludeTripId
            ORDER BY t.departure_time ASC
            """, nativeQuery = true)
    List<Trip> findUpcomingTripsByRouteExcludingTrip(@Param("routeId") Long routeId,
            @Param("excludeTripId") Long excludeTripId);

    boolean existsByDriverIdAndStatusIn(Long driverId, List<TripStatus> statuses);

    boolean existsByBusIdAndStatusIn(Long busId, List<TripStatus> statuses);

    @Query("""
            SELECT
                t.id as tripId,
                b.totalSeats as busSeatsCount,
                (SELECT COUNT(tk.ticketId) from Tickets tk
                 JOIN tk.booking bk
                 WHERE bk.trip.id = t.id AND tk.status = 'used') as checkedSeatsCount,
                (SELECT COUNT(tk.id) from Tickets tk
                 JOIN tk.booking bk
                 WHERE bk.trip.id = t.id AND tk.status = 'valid') as bookedSeatsCount
            FROM Trip t
            JOIN Bus b ON t.bus.id = b.id
            WHERE t.id = :tripId
            """)
    NextTripSeatStatusDTO getNextTripSeatStatus(@Param("tripId") Long tripId);
}
