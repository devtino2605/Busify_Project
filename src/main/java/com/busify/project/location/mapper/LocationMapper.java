package com.busify.project.location.mapper;

import com.busify.project.location.dto.response.LocationForOperatorResponse;
import com.busify.project.location.entity.Location;

public class LocationMapper {
    public static LocationForOperatorResponse toDTO(Location location) {
        if (location == null) {
            return null;
        }
        return LocationForOperatorResponse.builder()
                .locationId(location.getId())
                .locationName(location.getCity())
                .build();
    }
}
