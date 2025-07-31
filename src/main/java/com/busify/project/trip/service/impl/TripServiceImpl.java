package com.busify.project.trip.service.impl;

import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.bus_operator.repository.BusOperatorRepository;
import com.busify.project.review.repository.ReviewRepository;
import com.busify.project.route.dto.response.RouteResponse;
import com.busify.project.trip.dto.response.TripFilterResponseDTO;
import com.busify.project.trip.dto.request.TripFilterRequestDTO;
import com.busify.project.trip.dto.response.TopOperatorRatingDTO;
import com.busify.project.trip.dto.response.TripDetailResponse;
import com.busify.project.trip.dto.response.TripResponse;
import com.busify.project.trip.dto.response.TripRouteResponse;
import com.busify.project.trip.dto.response.TripStopResponse;
import com.busify.project.trip.entity.Trip;
import com.busify.project.trip.mapper.TripMapper;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.trip.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private BusOperatorRepository busOperatorRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public List<TripFilterResponseDTO> getAllTrips() {
        return tripRepository.findAll()
                .stream()
                .map(trip -> TripMapper.toDTO(trip, getAverageRating(trip.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<TripFilterResponseDTO> filterTrips(TripFilterRequestDTO filter) {
        List<Trip> trips = tripRepository.findAll().stream()
                .filter(trip -> filter.getRouteId() == null || trip.getRoute().getId().equals(filter.getRouteId()))
                .filter(trip -> filter.getOperatorId() == null ||
                        (trip.getBus() != null && trip.getBus().getOperator() != null &&
                                trip.getBus().getOperator().getId().equals(filter.getOperatorId())))
                .filter(trip -> filter.getMinPrice() == null
                        || trip.getPricePerSeat().compareTo(filter.getMinPrice()) >= 0)
                .filter(trip -> filter.getMaxPrice() == null
                        || trip.getPricePerSeat().compareTo(filter.getMaxPrice()) <= 0)
                .filter(trip -> filter.getSeatLayoutIds() == null ||
                        (trip.getBus() != null
                                && filter.getSeatLayoutIds().contains(trip.getBus().getSeatLayout().getId())))
                .filter(trip -> {
                    if (filter.getDepartureTime() == null) return true;
                    return trip.getDepartureTime()
                            .atZone(ZoneId.of("UTC"))
                            .withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"))
                            .toLocalDate()
                            .equals(filter.getDepartureTime());
                })
                .filter(trip -> {
                    if (filter.getDurationFilter() == null || trip.getEstimatedArrivalTime() == null) return true;
                    long durationHours = trip.getRoute().getDefaultDurationMinutes() / 60;
                    return switch (filter.getDurationFilter()) {
                        case "LESS_THAN_3" -> durationHours < 3;
                        case "BETWEEN_3_AND_6" -> durationHours >= 3 && durationHours <= 6;
                        case "BETWEEN_6_AND_12" -> durationHours >= 6 && durationHours <= 12;
                        case "GREATER_THAN_12" -> durationHours > 12;
                        default -> true;
                    };
                })
                .filter(trip -> {
                    if (filter.getAmenities() == null || trip.getBus() == null || trip.getBus().getAmenities() == null)
                        return true;
                    Map<String, Object> busAmenities = trip.getBus().getAmenities();
                    for (Map.Entry<String, Object> entry : filter.getAmenities().entrySet()) {
                        Object value = busAmenities.get(entry.getKey());
                        if (value == null || !value.equals(entry.getValue())) {
                            return false;
                        }
                    }
                    return true;
                })
                .filter(trip -> applyFilters(trip, filter))
                .toList();

        return trips.stream()
                .map(trip -> TripMapper.toDTO(trip, getAverageRating(trip.getId())))
                .collect(Collectors.toList());
    }

    private boolean applyFilters(Trip trip, TripFilterRequestDTO filter) {
        return true;
    }

    private Double getAverageRating(Long tripId) {
        Double rating = reviewRepository.findAverageRatingByTripId(tripId);
        return rating != null ? rating : 0.0;
    }

    public List<TripResponse> findTopUpcomingTripByOperator() {
        List<TopOperatorRatingDTO> operators = busOperatorRepository.findTopRatedOperatorId(PageRequest.of(0, 5));

        List<Trip> trips = new ArrayList<>();

        // Map để lưu rating của mỗi operator
        Map<Long, Double> operatorRatings = operators.stream()
                .collect(Collectors.toMap(TopOperatorRatingDTO::getOperatorId, TopOperatorRatingDTO::getAverageRating));

        for (TopOperatorRatingDTO operator : operators) {
            Trip trip = tripRepository.findUpcomingTripsByOperator(operator.getOperatorId(), Instant.now());
            if (trip != null) {
                trips.add(trip);
            }
        }
        List<TripResponse> tripsResponses = trips.stream().limit(4).map(trip -> TripResponse
                .builder()
                .trip_Id(trip.getId())
                .operator_name(trip.getBus().getOperator().getName()).route(
                        RouteResponse.builder()
                                .start_location(trip.getRoute().getStartLocation().getName())
                                .end_location(trip.getRoute().getEndLocation().getName())
                                .build())
                .arrival_time(trip.getEstimatedArrivalTime())
                .price_per_seat(trip.getPricePerSeat())
                .available_seats((int) (trip.getBus().getTotalSeats() - trip.getBookings().stream()
                        .filter(b -> b.getStatus() != BookingStatus.canceled_by_user
                                && b.getStatus() != BookingStatus.canceled_by_operator)
                        .count()))
                .departure_time(trip.getDepartureTime())
                .status(trip.getStatus())
                .average_rating(operatorRatings.get(trip.getBus().getOperator().getId()))
                .build()).collect(Collectors.toList());

        if (tripsResponses.isEmpty()) {
            return new ArrayList<>();
        }
        return tripsResponses;
    }

    @Override
    public Map<String, Object> getTripDetailById(Long tripId) {
        try {
            // get trip detail by ID
            TripDetailResponse tripDetail = tripRepository.findTripDetailById(tripId);
            // get trip stop by ID
            List<TripStopResponse> tripStops = tripRepository.findTripStopsById(tripId);
            // mapper to Map<String, Object> using mapper toTripDetail
            return TripMapper.toTripDetail(tripDetail, tripStops);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching trip detail for ID: " + tripId, e);
        }

    }

    @Override
    public List<TripRouteResponse> getTripRouteById(Long routeId) {
        try {
            return tripRepository.findUpcomingTripsByRoute(routeId);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching trip route for ID: " + routeId, e);

        }
    }

    @Override
    public List<TripStopResponse> getTripStopsById(Long tripId) {
        try {
            return tripRepository.findTripStopsById(tripId);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching trip stops for ID: " + tripId, e);
        }
    }

}