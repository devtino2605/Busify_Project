package com.busify.project.trip.service.impl;

import com.busify.project.trip.dto.response.TripListResponse;
import com.busify.project.trip.dto.response.TripResponse;
import com.busify.project.trip.entity.Trip;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {
    private final TripRepository tripRepository;
    @Override
    public List<TripResponse> getFeaturedTrips()
    {
        return List.of();
    }

    @Override
    public TripListResponse getTrips()
    {
        return null;
    }

    @Override
    public List<TripResponse> getHighRatedTripsForCurrentWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDateTime endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

//        List<Trip> highRatedTrips = tripRepository.findTopRatedTripsForCurrentWeek(startOfWeek, endOfWeek);

//        return highRatedTrips.stream()
//                .map(trip -> new TripResponse(
//                        trip.getId(),
//                        trip.getBus().getOperator().getName(),
//                        trip.getRoute(),
//                        trip.getDepartureTime(),
//                        trip.getEstimatedArrivalTime(),
//                        trip.getBus().getTotalSeats(),
//                        trip.getAverageRating(),
//                        trip.getStatus()))
//                .toList();
        return null;
    }
}
