package com.busify.project.trip.service.impl;

import com.busify.project.bus_operator.repository.BusOperatorRepository;
import com.busify.project.trip.dto.TripDTO;
import com.busify.project.trip.dto.TripFilterRequestDTO;
import com.busify.project.trip.dto.response.TopOperatorRatingDTO;
import com.busify.project.trip.dto.response.TripResponse;
import com.busify.project.trip.entity.Trip;
import com.busify.project.trip.mapper.TripMapper;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.trip.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.Duration;
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

    @Override
    public List<TripDTO> getAllTrips() {
        return tripRepository.findAll()
                .stream()
                .map(TripMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TripDTO> filterTrips(TripFilterRequestDTO filter) {
        List<Trip> trips = tripRepository.findAll().stream()
                .filter(trip -> filter.getRouteId() == null || trip.getRoute().getId().equals(filter.getRouteId()))
                .filter(trip -> filter.getOperatorId() == null ||
                        (trip.getBus() != null && trip.getBus().getOperator() != null &&
                                trip.getBus().getOperator().getId().equals(filter.getOperatorId())))
                .filter(trip -> filter.getMinPrice() == null || trip.getPricePerSeat().compareTo(filter.getMinPrice()) >= 0)
                .filter(trip -> filter.getMaxPrice() == null || trip.getPricePerSeat().compareTo(filter.getMaxPrice()) <= 0)
                .filter(trip -> filter.getSeatLayoutIds() == null ||
                        (trip.getBus() != null && filter.getSeatLayoutIds().contains(trip.getBus().getSeatLayout().getId())))
                .filter(trip -> {
                    if (filter.getDepartureTime() == null) return true;
                    return trip.getDepartureTime().atZone(ZoneId.of("UTC")).toLocalDate().equals(filter.getDepartureTime());
                })
                .filter(trip -> {
                    if (filter.getDurationFilter() == null || trip.getEstimatedArrivalTime() == null) return true;
                    long durationHours = Duration.between(trip.getDepartureTime(), trip.getEstimatedArrivalTime()).toHours();
                    return switch (filter.getDurationFilter()) {
                        case "LESS_THAN_3" -> durationHours < 3;
                        case "BETWEEN_3_AND_6" -> durationHours >= 3 && durationHours <= 6;
                        case "GREATER_THAN_6" -> durationHours > 6;
                        default -> true;
                    };
                })
                .filter(trip -> {
                    if (filter.getAmenities() == null || trip.getBus() == null || trip.getBus().getAmenities() == null) return true;
                    Map<String, Object> busAmenities = trip.getBus().getAmenities();
                    for (Map.Entry<String, Object> entry : filter.getAmenities().entrySet()) {
                        Object value = busAmenities.get(entry.getKey());
                        if (value == null || !value.equals(entry.getValue())) {
                            return false;
                        }
                    }
                    return true;
                })
                .toList();

        return trips.stream()
                .map(TripMapper::toDTO)
                .toList();
    }

    public List<TripResponse> findTopUpcomingTripByOperator()
    {
        List<TopOperatorRatingDTO> operators = busOperatorRepository.findTopRatedOperatorId(PageRequest.of(0, 5));

        List<Trip> trips = new ArrayList<>();

        // Map để lưu rating của mỗi operator
        Map<Long, Double> operatorRatings = operators.stream()
                .collect(Collectors.toMap(TopOperatorRatingDTO::getOperatorId, TopOperatorRatingDTO::getAverageRating));
        System.out.println(operatorRatings);

        for(TopOperatorRatingDTO operator : operators){
            Trip trip = tripRepository.findUpcomingTripsByOperator(operator.getOperatorId(), Instant.now());
            if (trip != null) {
                trips.add(trip);
            }
        }
        List<TripResponse> tripsResponses = trips.stream().map(trip -> TripResponse
                .builder()
                .tripId(trip.getId())
                .operatorName(trip.getBus().getOperator().getName())
                .arrivalTime(trip.getEstimatedArrivalTime())
                .availableSeats(trip.getBus().getTotalSeats())
                .departureTime(trip.getDepartureTime())
                .status(trip.getStatus())
                .averageRating(operatorRatings.get(trip.getBus().getOperator().getId()))
                .build()).collect(Collectors.toList());
        if(tripsResponses.isEmpty()){
            return new ArrayList<>();
        }
        return tripsResponses;
    }

}
