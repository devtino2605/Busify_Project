package com.busify.project.trip.service.impl;

import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.bus_operator.reponsitory.BusOperatorRepository;
import com.busify.project.route.dto.response.RouteResponse;
import com.busify.project.trip.dto.response.TopOperatorRatingDTO;
import com.busify.project.trip.dto.response.TripListResponse;
import com.busify.project.trip.dto.response.TripResponse;
import com.busify.project.trip.entity.Trip;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {
    private final TripRepository tripRepository;
    private final BusOperatorRepository busOperatorRepository;
    @Override
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
        List<TripResponse> tripsResponses = trips.stream().limit(4).map(trip -> TripResponse
            .builder()
            .trip_Id(trip.getId())
            .operator_name(trip.getBus().getOperator().getName()).route(
                RouteResponse.builder()
                    .start_location(trip.getRoute().getStartLocation().getName())
                    .end_location(trip.getRoute().getEndLocation().getName())
                    .build()
                )
            .arrival_time(trip.getEstimatedArrivalTime())
                .price_per_seat(trip.getPricePerSeat())
            .available_seats((int) (trip.getBus().getTotalSeats() -trip.getBookings().stream().filter(b -> b.getStatus() != BookingStatus.CANCELED_BY_USER && b.getStatus() != BookingStatus.CANCELED_BY_OPERATOR).count()))
            .departure_time(trip.getDepartureTime())
            .status(trip.getStatus())

            .average_rating(operatorRatings.get(trip.getBus().getOperator().getId()))
            .build()).collect(Collectors.toList());
        if(tripsResponses.isEmpty()){
            return new ArrayList<>();
        }
        return tripsResponses;
    }
}
