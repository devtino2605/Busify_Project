package com.busify.project.route_stop.entity;

import com.busify.project.location.entity.Location;
import com.busify.project.route.entity.Route;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "route_stops")
public class RouteStop {
    @EmbeddedId
    private RouteStopId id;

    @MapsId("routeId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @MapsId("locationId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "stop_order")
    private Integer stopOrder;

    @Column(name = "time_offset_from_start")
    private Integer timeOffsetFromStart;

}