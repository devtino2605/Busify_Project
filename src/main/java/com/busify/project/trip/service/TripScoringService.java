package com.busify.project.trip.service;

import com.busify.project.review.repository.ReviewRepository;
import com.busify.project.trip.dto.request.TripFilterRequestDTO;
import com.busify.project.trip.entity.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for calculating trip recommendation scores
 * Used when user selects sortBy = "recommended"
 */
@Service
@Slf4j
public class TripScoringService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Value("${trip.scoring.weight.time:0.4}")
    private double timeWeight;

    @Value("${trip.scoring.weight.price:0.3}")
    private double priceWeight;

    @Value("${trip.scoring.weight.rating:0.3}")
    private double ratingWeight;

    private static final int HOURS_PERFECT_MATCH = 0;
    private static final int HOURS_EXCELLENT = 1;
    private static final int HOURS_VERY_GOOD = 2;
    private static final int HOURS_GOOD = 4;
    private static final int HOURS_FAIR = 8;
    private static final int HOURS_ACCEPTABLE = 12;
    private static final int HOURS_OK = 24;

    /**
     * Calculate scores for all trips and return sorted list
     */
    public List<TripWithScore> scoreAndSort(List<Trip> trips, TripFilterRequestDTO preferences) {
        log.debug("Starting to score {} trips with preferences: {}", trips.size(), preferences);
        long startTime = System.currentTimeMillis();

        List<TripWithScore> scoredTrips = trips.stream()
                .map(trip -> {
                    double score = calculateScore(trip, preferences);
                    return new TripWithScore(trip, score);
                })
                .sorted(Comparator.comparing(TripWithScore::getScore).reversed())
                .collect(Collectors.toList());

        long duration = System.currentTimeMillis() - startTime;
        log.info("Scored {} trips in {}ms. Top score: {}, Bottom score: {}",
                trips.size(), duration,
                scoredTrips.isEmpty() ? 0 : scoredTrips.get(0).getScore(),
                scoredTrips.isEmpty() ? 0 : scoredTrips.get(scoredTrips.size() - 1).getScore());

        return scoredTrips;
    }

    /**
     * Calculate recommendation score for a single trip
     */
    private double calculateScore(Trip trip, TripFilterRequestDTO preferences) {
        double timeScore = calculateTimeScore(trip, preferences);
        double priceScore = calculatePriceScore(trip);
        double ratingScore = calculateRatingScore(trip);

        double finalScore = (timeScore * timeWeight) + (priceScore * priceWeight) + (ratingScore * ratingWeight);

        log.debug("Trip {} scores - Time: {}, Price: {}, Rating: {}, Final: {}",
                trip.getId(), timeScore, priceScore, ratingScore, finalScore);

        return finalScore;
    }

    /**
     * Time scoring: Closer to preferred time = higher score
     * Score range: 0-100
     */
    private double calculateTimeScore(Trip trip, TripFilterRequestDTO preferences) {
        if (preferences.getUntilTime() == null) {
            return 50.0;
        }

        LocalDateTime tripTime = trip.getDepartureTime();
        LocalDateTime preferredTime = preferences.getUntilTime();
        long hoursDiff = Math.abs(ChronoUnit.HOURS.between(tripTime, preferredTime));

        if (hoursDiff == HOURS_PERFECT_MATCH)
            return 100.0;
        if (hoursDiff <= HOURS_EXCELLENT)
            return 90.0;
        if (hoursDiff <= HOURS_VERY_GOOD)
            return 80.0;
        if (hoursDiff <= HOURS_GOOD)
            return 70.0;
        if (hoursDiff <= HOURS_FAIR)
            return 60.0;
        if (hoursDiff <= HOURS_ACCEPTABLE)
            return 50.0;
        if (hoursDiff <= HOURS_OK)
            return 40.0;
        return 30.0;
    }

    /**
     * Price scoring: Lower price = higher score
     * Score range: 0-100
     */
    private double calculatePriceScore(Trip trip) {
        double price = trip.getPricePerSeat().doubleValue();

        if (price <= 100000)
            return 100.0;
        if (price <= 200000)
            return 90.0;
        if (price <= 300000)
            return 80.0;
        if (price <= 400000)
            return 70.0;
        if (price <= 500000)
            return 60.0;
        if (price <= 700000)
            return 50.0;
        if (price <= 1000000)
            return 40.0;
        return 30.0;
    }

    /**
     * Rating scoring: Higher rating = higher score
     * Score range: 0-100
     * Fetches actual operator rating from database
     */
    private double calculateRatingScore(Trip trip) {
        try {
            Long operatorId = trip.getBus().getOperator().getId();
            Double averageRating = reviewRepository.findAverageRatingByOperatorId(operatorId);

            if (averageRating == null) {
                log.debug("No rating found for operator {}, using neutral score", operatorId);
                return 50.0;
            }

            double rating = averageRating;
            log.debug("Operator {} has rating: {}", operatorId, rating);

            if (rating >= 4.8)
                return 100.0;
            if (rating >= 4.5)
                return 90.0;
            if (rating >= 4.0)
                return 80.0;
            if (rating >= 3.5)
                return 70.0;
            if (rating >= 3.0)
                return 60.0;
            if (rating >= 2.5)
                return 50.0;
            if (rating >= 2.0)
                return 40.0;
            return 30.0;

        } catch (Exception e) {
            log.warn("Error fetching rating for trip {}: {}", trip.getId(), e.getMessage());
            return 50.0;
        }
    }

    /**
     * DTO to hold trip with its calculated score
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TripWithScore {
        private Trip trip;
        private double score;

        public int getScorePercentage() {
            return (int) Math.round(score);
        }
    }
}