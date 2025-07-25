


// package com.busify.project.route;

// import com.busify.project.route.dto.RouteResponse;
// import com.busify.project.route.entity.Route;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.stereotype.Repository;

// import java.util.List;

// @Repository
// public interface RouteRepository extends JpaRepository<Route, Long> {
//     // @Query("SELECT new com.busify.project.route.dto.RouteResponse(r.id, CONCAT(l1.name, ' → ', l2.name), " +
//     //        "CAST(CEIL(r.defaultDurationMinutes / 60.0) || '–' || CEIL(r.defaultDurationMinutes / 60.0 + 1) || ' hours' AS string), r.defaultPrice) " +
//     //        "FROM Route r JOIN r.startLocation l1 JOIN r.endLocation l2 ORDER BY r.defaultPrice ASC")

//     @Query("SELECT new com.busify.project.route.dto.RouteResponse(" +
//        "r.id, " +
//        "CONCAT(l1.name, ' → ', l2.name), " +
//        "CONCAT(CAST(CEIL(r.defaultDurationMinutes / 60.0) AS string), '–', " +
//        "CAST(CEIL(r.defaultDurationMinutes / 60.0 + 1) AS string), ' hours'), " +
//        "r.defaultPrice) " +
//        "FROM Route r " +
//        "JOIN r.startLocation l1 " +
//        "JOIN r.endLocation l2 " +
//        "ORDER BY r.defaultPrice ASC")

//     List<RouteResponse> findPopularRoutes();
    
// }


package com.busify.project.route;

import com.busify.project.route.dto.RouteResponse;
import com.busify.project.route.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    @Query("SELECT new com.busify.project.route.dto.RouteResponse(" +
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
    List<RouteResponse> findPopularRoutes();
}