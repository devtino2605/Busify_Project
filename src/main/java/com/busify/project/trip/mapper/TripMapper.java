package com.busify.project.trip.mapper;

import com.busify.project.trip.dto.response.RouteInfoResponseDTO;
import com.busify.project.trip.dto.response.TripFilterResponseDTO;
import com.busify.project.trip.entity.Trip;

public class TripMapper {

    public static TripFilterResponseDTO toDTO(Trip trip, Double averageRating) {
        if (trip == null) return null;

        TripFilterResponseDTO dto = new TripFilterResponseDTO();
        dto.setId(trip.getId());
        dto.setDepartureTime(trip.getDepartureTime());
        dto.setEstimatedArrivalTime(trip.getEstimatedArrivalTime());
        dto.setPricePerSeat(trip.getPricePerSeat());
        dto.setStatus(trip.getStatus());
        if (trip.getRoute() != null) {
            dto.setDuration(trip.getRoute().getDefaultDurationMinutes());

            RouteInfoResponseDTO routeDto = new RouteInfoResponseDTO();
            routeDto.setStartLocation(trip.getRoute().getStartLocation().getAddress()+"; "+trip.getRoute().getStartLocation().getCity());
            routeDto.setEndLocation(trip.getRoute().getEndLocation().getAddress()+"; "+trip.getRoute().getEndLocation().getCity());
            dto.setRoute(routeDto);
        }

        if (trip.getBus() != null) {
            dto.setOperatorName(trip.getBus().getOperator().getName());
            dto.setAmenities(trip.getBus().getAmenities());
        }

        dto.setAverageRating(averageRating != null ? averageRating : 0.0);

        return dto;
    }
}
