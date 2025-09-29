package com.busify.project.trip.service.impl;

import com.busify.project.bus.entity.Bus;
import com.busify.project.bus.repository.BusRepository;
import com.busify.project.bus_operator.repository.BusOperatorRepository;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.common.exception.ErrorCode;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.employee.entity.Employee;
import com.busify.project.employee.repository.EmployeeRepository;
import com.busify.project.route.entity.Route;
import com.busify.project.route.repository.RouteRepository;
import com.busify.project.seat_layout.entity.SeatLayout;
import com.busify.project.seat_layout.repository.SeatLayoutRepository;
import com.busify.project.trip.dto.request.TripMGMTRequestDTO;
import com.busify.project.trip.dto.response.ReportTripResponseDTO;
import com.busify.project.trip.dto.response.TripDeleteResponseDTO;
import com.busify.project.trip.dto.response.TripMGMTResponseDTO;
import com.busify.project.trip.entity.Trip;
import com.busify.project.trip.enums.TripStatus;
import com.busify.project.trip.exception.TripAccessException;
import com.busify.project.trip.exception.TripNotFoundException;
import com.busify.project.trip.exception.TripOperationException;
import com.busify.project.trip.mapper.TripMGMTMapper;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.trip.service.TripMGMTService;
import com.busify.project.trip_seat.entity.TripSeat;
import com.busify.project.trip_seat.entity.TripSeatId;
import com.busify.project.trip_seat.enums.TripSeatStatus;
import com.busify.project.trip_seat.repository.TripSeatRepository;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripMGMTServiceImpl implements TripMGMTService {

    private final TripRepository tripRepository;
    private final RouteRepository routeRepository;
    private final BusRepository busRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final SeatLayoutRepository seatLayoutRepository;
    private final TripSeatRepository tripSeatRepository;
    private final BusOperatorRepository busOperatorRepository;
    private final JwtUtils jwtUtil;

    @Override
    public TripMGMTResponseDTO addTrip(TripMGMTRequestDTO requestDTO) {
        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Long operatorId = 0L;

        // Nếu là OPERATOR
        if (user.getRole().getName().equals("OPERATOR")) {
            operatorId = busOperatorRepository.findOperatorIdByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy BusOperator cho user này"));
        }
        // Nếu là STAFF
        else if (user.getRole().getName().equals("STAFF")) {
            operatorId = employeeRepository.findOperatorIdByStaffUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Operator cho staff này"));
        }

        Trip trip = new Trip();

        Route route = routeRepository.findById(requestDTO.getRouteId())
                .orElseThrow(TripNotFoundException::routeNotFound);
        trip.setRoute(route);

        Bus bus = busRepository.findById(requestDTO.getBusId())
                .orElseThrow(TripNotFoundException::busNotFound);

        // Kiểm tra bus phải ACTIVE
        if (!"active".equalsIgnoreCase(bus.getStatus().name())) {
            throw TripOperationException.busNotActive();
        }

        if (operatorId != null && !bus.getOperator().getId().equals(operatorId)) {
            throw TripAccessException.busNotOwned();
        }
        trip.setBus(bus);

        Instant newDeparture = requestDTO.getDepartureTime();
        Instant newArrival = newDeparture.plus(Duration.ofMinutes(route.getDefaultDurationMinutes()));

        List<Trip> overlappingDriverTrips = tripRepository.findOverlappingTripsForDriver(
                requestDTO.getDriverId(), newDeparture, newArrival, -1L);

        if (!overlappingDriverTrips.isEmpty()) {
            throw TripOperationException.driverAlreadyAssigned();
        }

        List<Trip> overlappingBusTrips = tripRepository.findOverlappingTripsForBus(
                requestDTO.getBusId(), newDeparture, newArrival, -1L);

        if (!overlappingBusTrips.isEmpty()) {
            throw TripOperationException.busAlreadyAssigned();
        }

        Employee driver = employeeRepository.findById(requestDTO.getDriverId())
                .orElseThrow(TripNotFoundException::driverNotFound);

        // Kiểm tra driver phải ACTIVE
        if (!"active".equalsIgnoreCase(driver.getStatus().name())) {
            throw TripOperationException.driverNotActive();
        }

        trip.setDriver(driver);
        trip.setDepartureTime(requestDTO.getDepartureTime());
        trip.setEstimatedArrivalTime(
                requestDTO.getDepartureTime()
                        .plus(Duration.ofMinutes(route.getDefaultDurationMinutes())));
        trip.setStatus(requestDTO.getStatus());
        trip.setPricePerSeat(requestDTO.getPricePerSeat());

        Trip savedTrip = tripRepository.save(trip);

        SeatLayout seatLayout = seatLayoutRepository.findById(bus.getSeatLayout().getId())
                .orElseThrow(TripNotFoundException::seatLayoutNotFound);

        generateTripSeats(savedTrip, seatLayout);

        return TripMGMTMapper.toTripDetailResponseDTO(savedTrip);
    }

    private void generateTripSeats(Trip trip, SeatLayout seatLayout) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode layoutData = mapper.valueToTree(seatLayout.getLayoutData());

            int cols = layoutData.get("cols").asInt();
            int rows = layoutData.get("rows").asInt();
            int floors = layoutData.get("floors").asInt();

            List<TripSeat> tripSeats = new ArrayList<>();

            // Sinh tên ghế theo pattern: A.1.1 (col, row, floor)
            for (int floor = 1; floor <= floors; floor++) {
                for (int row = 1; row <= rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        char colChar = (char) ('A' + col);
                        String seatNumber = colChar + "." + row + "." + floor;

                        TripSeatId id = new TripSeatId(trip.getId(), seatNumber);

                        TripSeat seat = new TripSeat();
                        seat.setId(id);
                        seat.setStatus(TripSeatStatus.available); // enum
                        seat.setLockedAt(null);
                        seat.setLockingUser(null);

                        tripSeats.add(seat);
                    }
                }
            }

            tripSeatRepository.saveAll(tripSeats);
        } catch (Exception e) {
            throw TripOperationException.seatGenerationFailed(e);
        }
    }

    @Override
    public TripMGMTResponseDTO updateTrip(Long id, TripMGMTRequestDTO requestDTO) {
        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Long operatorId = 0L;

        // Nếu là OPERATOR
        if (user.getRole().getName().equals("OPERATOR")) {
            operatorId = busOperatorRepository.findOperatorIdByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy BusOperator cho user này"));
        }
        // Nếu là STAFF
        else if (user.getRole().getName().equals("STAFF")) {
            operatorId = employeeRepository.findOperatorIdByStaffUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Operator cho staff này"));
        }

        Trip trip = tripRepository.findById(id)
                .orElseThrow(TripNotFoundException::tripNotFound);

        if (requestDTO.getRouteId() != null) {
            Route route = routeRepository.findById(requestDTO.getRouteId())
                    .orElseThrow(TripNotFoundException::routeNotFound);
            trip.setRoute(route);
        }

        if (requestDTO.getBusId() != null) {
            Bus bus = busRepository.findById(requestDTO.getBusId())
                    .orElseThrow(TripNotFoundException::busNotFound);

            // Kiểm tra bus phải ACTIVE
            if (!"active".equalsIgnoreCase(bus.getStatus().name())) {
                throw TripOperationException.busNotActive();
            }

            if (operatorId != null && !bus.getOperator().getId().equals(operatorId)) {
                throw TripAccessException.busNotOwned();
            }

            Instant newDeparture = requestDTO.getDepartureTime() != null
                    ? requestDTO.getDepartureTime()
                    : trip.getDepartureTime();

            Instant newArrival = newDeparture.plus(Duration.ofMinutes(
                    trip.getRoute().getDefaultDurationMinutes()));

            List<Trip> overlappingBusTrips = tripRepository.findOverlappingTripsForBus(
                    requestDTO.getBusId(), newDeparture, newArrival, trip.getId());

            if (!overlappingBusTrips.isEmpty()) {
                throw TripOperationException.busAlreadyAssigned();
            }

            // Chỉ reset trip_seats nếu status là SCHEDULED
            TripStatus effectiveStatus = requestDTO.getStatus() != null
                    ? requestDTO.getStatus()
                    : trip.getStatus();

            if (effectiveStatus == TripStatus.scheduled) {
                tripSeatRepository.deleteByTripId(trip.getId());

                SeatLayout seatLayout = seatLayoutRepository.findById(bus.getSeatLayout().getId())
                        .orElseThrow(TripNotFoundException::seatLayoutNotFound);

                generateTripSeats(trip, seatLayout);
            }

            trip.setBus(bus);
        }

        if (requestDTO.getDriverId() != null) {
            Employee employee = employeeRepository.findById(requestDTO.getDriverId())
                    .orElseThrow(TripNotFoundException::driverNotFound);

            // Kiểm tra driver phải ACTIVE
            if (!"active".equalsIgnoreCase(employee.getStatus().name())) {
                throw TripOperationException.driverNotActive();
            }

            if (operatorId != null && !employee.getOperator().getId().equals(operatorId)) {
                throw new TripNotFoundException(ErrorCode.ACCESS_DENIED);
            }

            Instant newDeparture = requestDTO.getDepartureTime() != null
                    ? requestDTO.getDepartureTime()
                    : trip.getDepartureTime();

            Instant newArrival = newDeparture.plus(Duration.ofMinutes(
                    trip.getRoute().getDefaultDurationMinutes()));

            List<Trip> overlapping = tripRepository.findOverlappingTripsForDriver(
                    requestDTO.getDriverId(), newDeparture, newArrival, trip.getId());

            if (!overlapping.isEmpty()) {
                throw TripOperationException.driverAlreadyAssigned();
            }

            trip.setDriver(employee);
        }

        if (requestDTO.getDepartureTime() != null) {
            trip.setDepartureTime(requestDTO.getDepartureTime());
            Route route = trip.getRoute();
            if (route != null) {
                trip.setEstimatedArrivalTime(
                        requestDTO.getDepartureTime()
                                .plus(Duration.ofMinutes(route.getDefaultDurationMinutes())));
            }
        }

        if (requestDTO.getStatus() != null) {
            TripStatus newStatus = requestDTO.getStatus();

            // Nếu muốn set sang ARRIVED thì phải >= estimatedArrivalTime
//            if (newStatus == TripStatus.arrived) {
//                if (trip.getEstimatedArrivalTime() == null ||
//                        Instant.now().isBefore(trip.getEstimatedArrivalTime())) {
//                    throw TripOperationException.cannotArriveBeforeArrivalTime();
//                }
//            }

            trip.setStatus(newStatus);
        }

        if (requestDTO.getPricePerSeat() != null) {
            trip.setPricePerSeat(requestDTO.getPricePerSeat());
        }

        Trip updatedTrip = tripRepository.save(trip);
        return TripMGMTMapper.toTripDetailResponseDTO(updatedTrip);
    }

    @Override
    public TripDeleteResponseDTO deleteTrip(Long id, boolean isDelete) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(TripNotFoundException::tripNotFound);

        if (isDelete) {
            tripSeatRepository.deleteByTripId(trip.getId());
            tripRepository.delete(trip);
        }

        return new TripDeleteResponseDTO(
                trip.getId(),
                trip.getRoute() != null ? trip.getRoute().getId() : null,
                trip.getBus() != null ? trip.getBus().getId() : null);
    }

    @Override
    public ApiResponse<?> getAllTrips(String keyword, TripStatus status, String licensePlate, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);

        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Long operatorId = 0L;

        // Nếu là OPERATOR
        if (user.getRole().getName().equals("OPERATOR")) {
            operatorId = busOperatorRepository.findOperatorIdByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy BusOperator cho user này"));
        }
        // Nếu là STAFF
        else if (user.getRole().getName().equals("STAFF")) {
            operatorId = employeeRepository.findOperatorIdByStaffUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Operator cho staff này"));
        }

        Page<Trip> tripPage = tripRepository.searchAndFilterTrips(keyword, status, licensePlate, operatorId, pageable);

        List<TripMGMTResponseDTO> content = tripPage.stream()
                .map(TripMGMTMapper::toTripDetailResponseDTO)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("result", content);
        response.put("pageNumber", tripPage.getNumber() + 1);
        response.put("pageSize", tripPage.getSize());
        response.put("totalRecords", tripPage.getTotalElements());
        response.put("totalPages", tripPage.getTotalPages());
        response.put("hasNext", tripPage.hasNext());
        response.put("hasPrevious", tripPage.hasPrevious());

        return ApiResponse.success("Lấy danh sách chuyến đi thành công", response);
    }

    public ApiResponse<List<ReportTripResponseDTO>> reportTrips(Long operatorId) {
        return ApiResponse.success(
                "Get report data successfully",
                tripRepository.findReportTripByOperatorId(operatorId));
    }
}
