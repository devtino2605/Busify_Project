package com.busify.project.route_stop.repository;

import com.busify.project.route_stop.entity.RouteStop;
import com.busify.project.route_stop.entity.RouteStopId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop, RouteStopId> {
    
    /**
     * Tìm tất cả điểm dừng của một tuyến theo thứ tự stop_order
     */
    @Query("SELECT rs FROM RouteStop rs WHERE rs.route.id = :routeId ORDER BY rs.stopOrder ASC")
    List<RouteStop> findByRouteIdOrderByStopOrder(@Param("routeId") Long routeId);
    
    /**
     * Xóa tất cả điểm dừng của một tuyến
     */
    @Modifying
    @Query("DELETE FROM RouteStop rs WHERE rs.route.id = :routeId")
    void deleteByRouteId(@Param("routeId") Long routeId);
    
    /**
     * Kiểm tra xem một tuyến có điểm dừng hay không
     */
    boolean existsByRouteId(Long routeId);
}