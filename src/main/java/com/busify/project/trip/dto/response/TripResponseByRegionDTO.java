package com.busify.project.trip.dto.response;

import java.util.List;
import java.util.Map;

import com.busify.project.location.enums.LocationRegion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripResponseByRegionDTO {
    Map<LocationRegion, List<TripRouteResponse>> tripsByRegion;
}
