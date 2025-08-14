package com.busify.project.trip.service.impl;

import com.busify.project.bus.repository.BusRepository;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.employee.repository.EmployeeRepository;
import com.busify.project.route.entity.Route;
import com.busify.project.route.repository.RouteRepository;
import com.busify.project.trip.dto.request.TripMGMTRequestDTO;
import com.busify.project.trip.dto.response.TripDeleteResponseDTO;
import com.busify.project.trip.dto.response.TripMGMTResponseDTO;
import com.busify.project.trip.entity.Trip;
import com.busify.project.trip.enums.TripStatus;
import com.busify.project.trip.mapper.TripMGMTMapper;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.trip.service.TripMGMTService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Duration;
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

    @Override
    public TripMGMTResponseDTO addTrip(TripMGMTRequestDTO requestDTO) {
        Trip trip = new Trip();

        Route route = routeRepository.findById(requestDTO.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route không tồn tại"));
        trip.setRoute(route);

        trip.setRoute(routeRepository.findById(requestDTO.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route không tồn tại")));

        trip.setBus(busRepository.findById(requestDTO.getBusId())
                .orElseThrow(() -> new RuntimeException("Bus không tồn tại")));

        trip.setDriver(employeeRepository.findById(requestDTO.getDriverId())
                .orElseThrow(() -> new RuntimeException("Tài xế không tồn tại")));

        trip.setDepartureTime(requestDTO.getDepartureTime());
        // Tính estimatedArrivalTime = departureTime + default_duration_minutes
        trip.setEstimatedArrivalTime(
                requestDTO.getDepartureTime()
                        .plus(Duration.ofMinutes(route.getDefaultDurationMinutes()))
        );
        trip.setStatus(requestDTO.getStatus());
        trip.setPricePerSeat(requestDTO.getPricePerSeat());

        Trip savedTrip = tripRepository.save(trip);

        return TripMGMTMapper.toTripDetailResponseDTO(savedTrip);
    }

    @Override
    public TripMGMTResponseDTO updateTrip(Long id, TripMGMTRequestDTO requestDTO) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip không tồn tại"));

        // Chỉ update route nếu có routeId truyền vào
        if (requestDTO.getRouteId() != null) {
            Route route = routeRepository.findById(requestDTO.getRouteId())
                    .orElseThrow(() -> new RuntimeException("Route không tồn tại"));
            trip.setRoute(route);
        }

        if (requestDTO.getBusId() != null) {
            trip.setBus(busRepository.findById(requestDTO.getBusId())
                    .orElseThrow(() -> new RuntimeException("Bus không tồn tại")));
        }

        if (requestDTO.getDriverId() != null) {
            trip.setDriver(employeeRepository.findById(requestDTO.getDriverId())
                    .orElseThrow(() -> new RuntimeException("Tài xế không tồn tại")));
        }

        if (requestDTO.getDepartureTime() != null) {
            trip.setDepartureTime(requestDTO.getDepartureTime());
            Route route = trip.getRoute();
            if (route != null) {
                trip.setEstimatedArrivalTime(
                        requestDTO.getDepartureTime()
                                .plus(Duration.ofMinutes(route.getDefaultDurationMinutes()))
                );
            }
        }

        if (requestDTO.getStatus() != null) {
            trip.setStatus(requestDTO.getStatus());
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
                .orElseThrow(() -> new RuntimeException("Trip không tồn tại"));

        if (isDelete) {
            tripRepository.delete(trip);
        }

        return new TripDeleteResponseDTO(
                trip.getId(),
                trip.getRoute() != null ? trip.getRoute().getId() : null,
                trip.getBus() != null ? trip.getBus().getId() : null
        );
    }

    @Override
    public ApiResponse<?> getAllTrips(String keyword, TripStatus status, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);

        Page<Trip> tripPage = tripRepository.searchAndFilterTrips(keyword, status, pageable);

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
}
