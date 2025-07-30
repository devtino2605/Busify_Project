// package com.busify.project.trip_seat.services;

// import com.busify.project.bus.entity.Bus;
// import com.busify.project.bus.repository.BusRepository;
// import com.busify.project.seat_layout.entity.SeatLayout;
// import com.busify.project.seat_layout.repository.SeatLayoutRepository;
// import com.busify.project.trip.entity.Trip;
// import com.busify.project.trip.repository.TripRepository;
// import com.busify.project.trip_seat.dto.SeatResponse;
// import com.busify.project.trip_seat.entity.TripSeat;
// import com.busify.project.trip_seat.entity.TripSeatId;
// import com.busify.project.trip_seat.repository.TripSeatRepository;
// import com.fasterxml.jackson.databind.JsonNode;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import com.busify.project.trip_seat.enums.TripSeatStatus;

// import java.util.ArrayList;
// import java.util.List;

// @Service
// public class TripSeatService {
//     @Autowired
//     private TripSeatRepository tripSeatRepository;

//     @Autowired
//     private TripRepository tripRepository;

//     @Autowired
//     private BusRepository busRepository;

//     @Autowired
//     private SeatLayoutRepository seatLayoutRepository;

//     @Transactional(readOnly = true)
//     public List<SeatResponse> getSeatAvailability(Long tripId) {
//         // Kiểm tra trip có tồn tại không
//         Trip trip = tripRepository.findById(tripId)
//                 .orElseThrow(() -> new IllegalArgumentException("Trip not found"));

//         // Lấy thông tin xe
//         Bus bus = busRepository.findById(trip.getBus().getId())
//                 .orElseThrow(() -> new IllegalArgumentException("Bus not found"));
//         SeatLayout seatLayout = bus.getSeatLayout(); // Lấy đối tượng SeatLayout
//         if (seatLayout == null) {
//             throw new IllegalArgumentException("Seat layout is null for bus ID: " + bus.getId());
//         }
//         JsonNode layoutData = (JsonNode) seatLayout.getLayoutData();
//         List<String> seatNodes = layoutData.get("seats").findValuesAsText("number");

//         // Lấy danh sách ghế đã có trong trip_seats
//         List<TripSeat> existingSeats = tripSeatRepository.findByTripId(tripId);

//         // Tạo danh sách tất cả ghế từ layout_data
//         List<SeatResponse> allSeats = new ArrayList<>();
//         for (String seatNumber : seatNodes) {
//             TripSeat seat = existingSeats.stream()
//                 .filter(s -> s.getId().getSeatNumber().equals(seatNumber))
//                 .findFirst()
//                 .orElse(null);
//             String status = (seat != null) ? seat.getStatus().name().toLowerCase() : "available";
//             boolean isBooked = (seat != null && seat.getStatus() == TripSeatStatus.BOOKED);
//             allSeats.add(new SeatResponse(seatNumber, status, isBooked));
//         }

//         return allSeats;
//     }
// }

// package com.busify.project.trip_seat.services;

// import com.busify.project.bus.entity.Bus;
// import com.busify.project.bus.repository.BusRepository;
// import com.busify.project.seat_layout.entity.SeatLayout;
// import com.busify.project.seat_layout.repository.SeatLayoutRepository;
// import com.busify.project.trip.entity.Trip;
// import com.busify.project.trip.repository.TripRepository;
// import com.busify.project.trip_seat.dto.SeatResponse;
// import com.busify.project.trip_seat.entity.TripSeat;
// import com.busify.project.trip_seat.enums.TripSeatStatus;
// import com.busify.project.trip_seat.repository.TripSeatRepository;
// import com.fasterxml.jackson.databind.JsonNode;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.util.ArrayList;
// import java.util.List;

// @Service
// public class TripSeatService {
//     @Autowired
//     private TripSeatRepository tripSeatRepository;

//     @Autowired
//     private TripRepository tripRepository;

//     @Autowired
//     private BusRepository busRepository;

//     @Autowired
//     private SeatLayoutRepository seatLayoutRepository;

//     @Transactional(readOnly = true)
//     public List<SeatResponse> getSeatAvailability(Long tripId) {
//         Trip trip = tripRepository.findById(tripId)
//             .orElseThrow(() -> new IllegalArgumentException("Trip not found"));

//         Bus bus = busRepository.findById(trip.getBus().getId()) // Sửa thành trip.getBus().getId()
//             .orElseThrow(() -> new IllegalArgumentException("Bus not found"));
//         SeatLayout seatLayout = bus.getSeatLayout();
//         if (seatLayout == null) {
//             throw new IllegalArgumentException("Seat layout is null for bus ID: " + bus.getId());
//         }
//         JsonNode layoutData = seatLayout.getLayoutData();
//         List<String> seatNodes = layoutData.get("seats").findValuesAsText("number");

//         List<TripSeat> existingSeats = tripSeatRepository.findByTripId(tripId);

//         List<SeatResponse> allSeats = new ArrayList<>();
//         for (String seatNumber : seatNodes) {
//             TripSeat seat = existingSeats.stream()
//                 .filter(s -> s.getId().getSeatNumber().equals(seatNumber))
//                 .findFirst()
//                 .orElse(null);
//             String status = (seat != null) ? seat.getStatus().name().toLowerCase() : "available";
//             boolean isBooked = (seat != null && seat.getStatus() == TripSeatStatus.BOOKED);
//             allSeats.add(new SeatResponse(seatNumber, status, isBooked));
//         }

//         return allSeats;
//     }
// }

package com.busify.project.trip_seat.services;

import com.busify.project.trip_seat.enums.TripSeatStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.busify.project.bus.entity.Bus;
import com.busify.project.bus.repository.BusRepository;
import com.busify.project.seat_layout.entity.SeatLayout;
import com.busify.project.seat_layout.repository.SeatLayoutRepository;
import com.busify.project.trip.entity.Trip;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.trip_seat.dto.SeatLayoutResponse;
import com.busify.project.trip_seat.dto.SeatResponse;
import com.busify.project.trip_seat.entity.TripSeat;
import com.busify.project.trip_seat.entity.TripSeatId;
import com.busify.project.trip_seat.repository.TripSeatRepository;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TripSeatService {
    @Autowired
    private TripSeatRepository tripSeatRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private ObjectMapper objectMapper; // Tiêm ObjectMapper

    @Autowired
    private SeatLayoutRepository seatLayoutRepository;

    // @Transactional(readOnly = true)
    // public /*List<SeatResponse>*/ SeatLayoutResponse getSeatAvailability(Long
    // tripId) {
    // Trip trip = tripRepository.findById(tripId)
    // .orElseThrow(() -> new IllegalArgumentException("Trip not found"));

    // Bus bus = busRepository.findById(trip.getBus().getId())
    // .orElseThrow(() -> new IllegalArgumentException("Bus not found"));
    // SeatLayout seatLayout = bus.getSeatLayout();
    // if (seatLayout == null) {
    // throw new IllegalArgumentException("Seat layout is null for bus ID: " +
    // bus.getId());
    // }

    // // Chuyển đổi layoutData thành JsonNode
    // Object layoutDataObj = seatLayout.getLayoutData();
    // JsonNode layoutData = objectMapper.convertValue(layoutDataObj,
    // JsonNode.class);

    // int rows = layoutData.get("rows").asInt();
    // int columns = layoutData.get("columns").asInt();

    // List<String> seatNodes = layoutData.get("seats").findValuesAsText("number");

    // List<TripSeat> existingSeats = tripSeatRepository.findByTripId(tripId);

    // List<SeatResponse> allSeats = new ArrayList<>();
    // for (String seatNumber : seatNodes) {
    // TripSeat seat = existingSeats.stream()
    // .filter(s -> s.getId().getSeatNumber().equals(seatNumber))
    // .findFirst()
    // .orElse(null);
    // String status = (seat != null) ? seat.getStatus().name().toLowerCase() :
    // "available";
    // boolean isBooked = (seat != null && seat.getStatus() ==
    // TripSeatStatus.booked);
    // allSeats.add(new SeatResponse(seatNumber, status, isBooked));
    // }

    // return /*allSeats;*/ new SeatLayoutResponse(rows, columns, seats);
    // }

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
        JsonNode layoutData = objectMapper.convertValue(layoutDataObj, JsonNode.class);
        int rows = layoutData.get("rows").asInt();
        int columns = layoutData.get("columns").asInt();
        List<String> seatNumbers = layoutData.get("seats").findValuesAsText("number");

        List<TripSeat> existingSeats = tripSeatRepository.findByTripId(tripId);
        List<SeatResponse> seats = new ArrayList<>();
        for (String seatNumber : seatNumbers) {
            TripSeat seat = existingSeats.stream()
                    .filter(s -> s.getId().getSeatNumber().equals(seatNumber))
                    .findFirst()
                    .orElse(null);
            String status = (seat != null) ? seat.getStatus().name().toLowerCase() : "available";
            boolean isBooked = (seat != null && seat.getStatus() == TripSeatStatus.booked);
            seats.add(new SeatResponse(seatNumber, status, isBooked));
        }

        return new SeatLayoutResponse(rows, columns, seats);
    }
}