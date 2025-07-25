package com.busify.project.trip.mapper;

import com.busify.project.trip.dto.TripDTO;
import com.busify.project.trip.entity.Trip;

public class TripMapper {

    public static TripDTO toDTO(Trip trip) {
        if (trip == null) return null;

        TripDTO dto = new TripDTO();
        dto.setId(trip.getId());
        dto.setDepartureTime(trip.getDepartureTime());
        dto.setEstimatedArrivalTime(trip.getEstimatedArrivalTime());
        dto.setPricePerSeat(trip.getPricePerSeat());
        dto.setStatus(trip.getStatus());

        if (trip.getRoute() != null) {
            dto.setRouteId(trip.getRoute().getId());
            dto.setRouteName(trip.getRoute().getName());
            dto.setDuration(trip.getRoute().getDefaultDurationMinutes());
        }

        if (trip.getBus() != null) {
            dto.setBusId(trip.getBus().getId());
            dto.setBusPlateNumber(trip.getBus().getLicensePlate());
            dto.setOperatorId(trip.getBus().getOperator().getId());
            dto.setSeatLayoutId(trip.getBus().getSeatLayout().getId());
            dto.setSeatLayoutName(trip.getBus().getSeatLayout().getName());
            dto.setAmenities(trip.getBus().getAmenities());
        }

        return dto;
    }
}
