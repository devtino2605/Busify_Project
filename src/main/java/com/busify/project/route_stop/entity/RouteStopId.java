package com.busify.project.route_stop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class RouteStopId implements java.io.Serializable {
    private static final long serialVersionUID = 4024646763236586448L;
    @Column(name = "route_id", nullable = false)
    private Long routeId;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RouteStopId entity = (RouteStopId) o;
        return Objects.equals(this.routeId, entity.routeId) &&
                Objects.equals(this.locationId, entity.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeId, locationId);
    }

}