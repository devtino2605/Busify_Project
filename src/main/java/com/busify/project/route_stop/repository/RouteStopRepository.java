package com.busify.project.route_stop.repository;

import com.busify.project.route_stop.entity.RouteStop;
import com.busify.project.route_stop.entity.RouteStopId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop, RouteStopId> {
    List<RouteStop> findByRoute_IdOrderByStopOrderAsc(Long routeId);
    boolean existsByRoute_IdAndLocation_Id(Long routeId, Long locationId);
}
