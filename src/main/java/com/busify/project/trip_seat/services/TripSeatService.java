
package com.busify.project.trip_seat.services;

import com.busify.project.trip_seat.enums.TripSeatStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.busify.project.bus.entity.Bus;
import com.busify.project.bus.repository.BusRepository;
import com.busify.project.seat_layout.entity.SeatLayout;
import com.busify.project.trip.entity.Trip;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.trip_seat.dto.SeatLayoutResponse;
import com.busify.project.trip_seat.dto.SeatResponse;
import com.busify.project.trip_seat.entity.TripSeat;
import com.busify.project.trip_seat.repository.TripSeatRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TripSeatService {
    private static final Logger logger = LoggerFactory.getLogger(TripSeatService.class);

    @Autowired
    private TripSeatRepository tripSeatRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public SeatLayoutResponse getSeatAvailability(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));
        Bus bus = busRepository.findById(trip.getBus().getId())
                .orElseThrow(() -> new IllegalArgumentException("Bus not found"));
        SeatLayout seatLayout = bus.getSeatLayout();
        if (seatLayout == null) {
            throw new IllegalArgumentException("Seat layout is null for bus ID: " + bus.getId());
        }

        Object layoutDataObj = seatLayout.getLayoutData();
        if (layoutDataObj == null) {
            throw new IllegalArgumentException("Layout data is null for seat layout ID: " + seatLayout.getId());
        }

        JsonNode layoutData = objectMapper.convertValue(layoutDataObj, JsonNode.class);
        logger.info("Layout data: {}", layoutData.toString());

        JsonNode resultNode = layoutData.get("result");
        if (resultNode == null) {
            throw new IllegalArgumentException("Invalid layout data: missing 'result' node");
        }

        JsonNode rowsNode = resultNode.get("rows");
        JsonNode columnsNode = resultNode.get("columns");
        JsonNode floorsNode = resultNode.get("floors");
        JsonNode seatsNode = resultNode.get("seats");

        if (rowsNode == null || columnsNode == null || floorsNode == null || seatsNode == null) {
            throw new IllegalArgumentException("Invalid layout data: missing rows, columns, floors, or seats");
        }

        int rows = rowsNode.asInt();
        int columns = columnsNode.asInt();
        int floors = floorsNode.asInt();

        List<TripSeat> existingSeats = tripSeatRepository.findByTripId(tripId);
        List<SeatResponse> seats = new ArrayList<>();

        // Duyệt qua từng ghế trong seatsNode
        for (JsonNode seatNode : seatsNode) {
            String seatNumber = seatNode.get("seatNumber").asText();
            String row = String.valueOf(seatNode.get("row").asInt()); // Chuyển int thành String
            String column = String.valueOf(seatNode.get("column").asInt()); // Chuyển int thành String
            String floor = String.valueOf(seatNode.get("floor").asInt()); // Chuyển int thành String

            TripSeat tripSeat = existingSeats.stream()
                    .filter(s -> s.getId().getSeatNumber().equals(seatNumber))
                    .findFirst()
                    .orElse(null);

            String status = (tripSeat != null) ? tripSeat.getStatus().name().toLowerCase() : "available";
            boolean isBooked = (tripSeat != null && tripSeat.getStatus() == TripSeatStatus.booked);

            seats.add(new SeatResponse(seatNumber, status, isBooked, row, column, floor));
        }

        return new SeatLayoutResponse(rows, columns, floors, seats);
    }
}