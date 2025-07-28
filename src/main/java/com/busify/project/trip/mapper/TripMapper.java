package com.busify.project.trip.mapper;

import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.trip.dto.response.RouteInfoResponseDTO;
import com.busify.project.trip.dto.response.TripFilterResponseDTO;
import com.busify.project.trip.entity.Trip;

import java.util.Arrays;
import java.util.List;

public class TripMapper {

    public static TripFilterResponseDTO toDTO(Trip trip, Double averageRating, BookingRepository bookingRepository) {
        if (trip == null) return null;

        TripFilterResponseDTO dto = new TripFilterResponseDTO();
        dto.setTrip_id(trip.getId());
        dto.setDeparture_time(trip.getDepartureTime());
        dto.setArrival_time(trip.getEstimatedArrivalTime());
        dto.setPrice_per_seat(trip.getPricePerSeat());
        dto.setStatus(trip.getStatus());
        if (trip.getRoute() != null) {
            dto.setDuration(trip.getRoute().getDefaultDurationMinutes());

            RouteInfoResponseDTO routeDto = new RouteInfoResponseDTO();
            routeDto.setStart_location(trip.getRoute().getStartLocation().getAddress()+"; "+trip.getRoute().getStartLocation().getCity());
            routeDto.setEnd_location(trip.getRoute().getEndLocation().getAddress()+"; "+trip.getRoute().getEndLocation().getCity());
            dto.setRoute(routeDto);
        }

        if (trip.getBus() != null) {
            dto.setOperator_name(trip.getBus().getOperator().getName());
            dto.setAmenities(trip.getBus().getAmenities());
        }

        dto.setAverage_rating(averageRating != null ? averageRating : 0.0);

        List<BookingStatus> canceledStatuses = Arrays.asList(
                BookingStatus.canceled_by_user,
                BookingStatus.canceled_by_operator
        );
        int bookedSeats = bookingRepository.countBookedSeats(trip.getId(), canceledStatuses);
        int totalSeats = trip.getBus().getTotalSeats();
        dto.setAvailable_seats(totalSeats - bookedSeats);


        return dto;
    }
}
