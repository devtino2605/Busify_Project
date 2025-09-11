package com.busify.project.route.repository;

import com.busify.project.location.entity.Location;
import com.busify.project.route.dto.response.PopularRouteResponse;
import com.busify.project.route.dto.response.TopRouteRevenueDTO;
import com.busify.project.route.entity.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    @Query("SELECT new com.busify.project.route.dto.response.PopularRouteResponse(" +
            "r.id, " +
            "CONCAT(l1.name, ' → ', l2.name), " +
            "CONCAT(CAST(CEIL(r.defaultDurationMinutes / 60.0) AS string), '–', " +
            "CAST(CEIL(r.defaultDurationMinutes / 60.0 + 1) AS string), ' hours'), " +
            "r.defaultPrice) " +
            "FROM Route r " +
            "JOIN r.startLocation l1 " +
            "JOIN r.endLocation l2 " +
            "LEFT JOIN Trip t ON r.id = t.route.id " +
            "WHERE t.status != 'cancelled' OR t.status IS NULL " +
            "GROUP BY r.id, l1.name, l2.name, r.defaultDurationMinutes, r.defaultPrice " +
            "ORDER BY COUNT(t.id) DESC")
    List<PopularRouteResponse> findPopularRoutes();


    @Query("""
                SELECT r FROM Route r
                WHERE (:keyword IS NULL OR :keyword = '' 
                       OR LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Route> searchRoutes(@Param("keyword") String keyword, Pageable pageable);

    // Query để lấy top 10 routes có doanh thu cao nhất theo năm
    @Query(value = """
            SELECT
                r.route_id as routeId,
                CONCAT(sl.name, ' → ', el.name) as routeName,
                sl.name as startLocation,
                el.name as endLocation,
                COUNT(DISTINCT t.trip_id) as totalTrips,
                COUNT(b.id) as totalBookings,
                COALESCE(SUM(b.total_amount), 0) as totalRevenue,
                CASE
                    WHEN COUNT(DISTINCT t.trip_id) > 0
                    THEN COALESCE(SUM(b.total_amount), 0) / COUNT(DISTINCT t.trip_id)
                    ELSE 0
                END as averageRevenuePerTrip
            FROM routes r
            LEFT JOIN locations sl ON r.start_location_id = sl.location_id
            LEFT JOIN locations el ON r.end_location_id = el.location_id
            LEFT JOIN trips t ON r.route_id = t.route_id
            LEFT JOIN bookings b ON t.trip_id = b.trip_id
                AND b.status IN ('confirmed', 'completed')
                AND YEAR(b.created_at) = :year
            GROUP BY r.route_id, sl.name, el.name
            HAVING COUNT(DISTINCT t.trip_id) > 0
            ORDER BY totalRevenue DESC
            LIMIT 10
            """, nativeQuery = true)
    List<TopRouteRevenueDTO> findTop10RoutesByRevenueAndYear(@Param("year") Integer year);

    boolean existsByStartLocationAndEndLocation(Location startLocation, Location endLocation);

    boolean existsByStartLocationAndEndLocationAndIdNot(Location startLocation, Location endLocation, Long id);

}