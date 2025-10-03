package com.busify.project.trip.mapper;

import com.busify.project.trip.dto.response.TripMGMTResponseDTO;
import com.busify.project.trip.entity.Trip;
import com.busify.project.trip_seat.services.TripSeatService;
import org.springframework.stereotype.Component;

@Component
public class TripMGMTMapper {

    private static TripSeatService tripSeatService = null;

    public TripMGMTMapper(TripSeatService tripSeatService) {
        this.tripSeatService = tripSeatService;
    }

    public static TripMGMTResponseDTO toTripDetailResponseDTO(Trip trip) {
        if (trip == null) return null;

        TripMGMTResponseDTO dto = new TripMGMTResponseDTO();
        dto.setId(trip.getId());
        dto.setRouteId(trip.getRoute() != null ? trip.getRoute().getId() : null);
        dto.setRouteName(trip.getRoute() != null ? trip.getRoute().getName() : null);
        dto.setBusId(trip.getBus() != null ? trip.getBus().getId() : null);
        dto.setLicensePlate(trip.getBus() != null ? trip.getBus().getLicensePlate() : null);
        dto.setDriverId(trip.getDriver() != null ? trip.getDriver().getId() : null);
        dto.setDriverName(trip.getDriver() != null ? trip.getDriver().getFullName() : null);
        dto.setDepartureTime(trip.getDepartureTime());
        dto.setEstimatedArrivalTime(trip.getEstimatedArrivalTime());
        dto.setStatus(trip.getStatus());
        dto.setPricePerSeat(trip.getPricePerSeat());
        dto.setTotalSeats(trip.getBus().getTotalSeats());
        dto.setAvailableSeats(tripSeatService.countAvailableSeats(trip.getId()));

        return dto;
    }
}
