package com.busify.project.trip.service.impl;

import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.bus_operator.repository.BusOperatorRepository;
import com.busify.project.review.repository.ReviewRepository;
import com.busify.project.route.dto.response.RouteResponse;
import com.busify.project.trip.dto.response.TripFilterResponseDTO;
import com.busify.project.trip.dto.request.TripFilterRequestDTO;
import com.busify.project.trip.dto.response.NextTripsOfOperatorResponseDTO;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public List<TripFilterResponseDTO> getAllTrips() {
        return tripRepository.findAll()
                .stream()
                .map(trip -> TripMapper.toDTO(trip, getAverageRating(trip.getId()), bookingRepository))
                .collect(Collectors.toList());
    }

    @Override
    public List<TripFilterResponseDTO> filterTrips(TripFilterRequestDTO filter) {
        List<Trip> trips = tripRepository.findAll().stream()
                .filter(trip -> filter.getRouteId() == null || trip.getRoute().getId().equals(filter.getRouteId()))
                .filter(trip -> filter.getBusOperatorIds() == null
                        || List.of(filter.getBusOperatorIds()).contains(trip.getBus().getOperator().getId()))
                .filter(trip -> filter.getDepartureDate() == null
                        || trip.getDepartureTime().atZone(ZoneId.of("Asia/Ho_Chi_Minh"))
                                .toLocalDate().isEqual(LocalDate.parse(filter.getDepartureDate())))
                .filter(trip -> filter.getUntilTime() == null
                        || trip.getDepartureTime().atZone(ZoneId.of("Asia/Ho_Chi_Minh"))
                                .toLocalTime().isBefore(LocalTime.parse(filter.getUntilTime())))
                .filter(trip -> filter.getAvailableSeats() == null
                        || trip.getBus().getTotalSeats() - trip.getBookings().stream()
                                .filter(b -> b.getStatus() != BookingStatus.canceled_by_user
                                        && b.getStatus() != BookingStatus.canceled_by_operator)
                                .count() >= filter.getAvailableSeats())
                .filter(trip -> filter.getBusModels() == null
                        || List.of(filter.getBusModels()).contains(trip.getBus().getModel()))
                .filter(trip -> filter.getAmenities() == null
                        || Arrays.stream(filter.getAmenities())
                                .allMatch(a -> trip.getBus().getAmenities().containsKey(a)
                                        || trip.getBus().getAmenities().containsValue(a)))
                .filter(trip -> applyFilters(trip, filter))
                .collect(Collectors.toList());
        if (trips.isEmpty()) {
            return new ArrayList<>();
        }
        return trips.stream()
                .map(trip -> TripMapper.toDTO(trip, getAverageRating(trip.getId()), bookingRepository))
                .collect(Collectors.toList());
    }

    private boolean applyFilters(Trip trip, TripFilterRequestDTO filter) {
        return true;
    }

    private Double getAverageRating(Long tripId) {
        Double rating = reviewRepository.findAverageRatingByTripId(tripId);
        if (rating == null)
            return 0.0;

        return Math.round(rating * 10.0) / 10.0;
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
                .trip_id(trip.getId())
                .operator_name(trip.getBus().getOperator().getName()).route(
                        RouteResponse.builder()
                                .start_location(trip.getRoute().getStartLocation().getName())
                                .end_location(trip.getRoute().getEndLocation().getName())
                                .default_duration_minutes(trip.getRoute().getDefaultDurationMinutes())
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

    public List<Map<String, Object>> getNextTripsOfOperator(Long operatorId) {
        List<NextTripsOfOperatorResponseDTO> nextTrips = tripRepository.findNextTripsByOperator(operatorId);
        if (nextTrips.isEmpty()) {
            return List.of(Map.of("message", "No upcoming trips found for this operator."));
        }

        return nextTrips.stream().map(TripMapper::toNextTripsOfOperatorResponse).collect(Collectors.toList());
    }
}