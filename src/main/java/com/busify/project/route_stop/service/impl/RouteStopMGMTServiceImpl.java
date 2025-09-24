package com.busify.project.route_stop.service.impl;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.location.entity.Location;
import com.busify.project.location.repository.LocationRepository;
import com.busify.project.route.entity.Route;
import com.busify.project.route.repository.RouteRepository;
import com.busify.project.route_stop.dto.request.RouteStopMGMTRequestDTO;
import com.busify.project.route_stop.dto.response.RouteStopDeleteResponseDTO;
import com.busify.project.route_stop.dto.response.RouteStopMGMTResponseDTO;
import com.busify.project.route_stop.entity.RouteStop;
import com.busify.project.route_stop.entity.RouteStopId;
import com.busify.project.route_stop.mapper.RouteStopMGMTMapper;
import com.busify.project.route_stop.repository.RouteStopRepository;
import com.busify.project.route_stop.service.RouteStopMGMTService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteStopMGMTServiceImpl implements RouteStopMGMTService {

    private final RouteStopRepository routeStopRepository;
    private final RouteRepository routeRepository;
    private final LocationRepository locationRepository;

    @Override
    public RouteStopMGMTResponseDTO addRouteStop(RouteStopMGMTRequestDTO requestDTO) {
        Route route = routeRepository.findById(requestDTO.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route không tồn tại"));
        Location location = locationRepository.findById(requestDTO.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location không tồn tại"));

        if (routeStopRepository.existsByRoute_IdAndLocation_Id(route.getId(), location.getId())) {
            throw new RuntimeException("Điểm dừng này đã tồn tại trong tuyến");
        }

        // Kiểm tra timeOffsetFromStart <= defaultDurationMinutes
        if (requestDTO.getTimeOffsetFromStart() > route.getDefaultDurationMinutes()) {
            throw new RuntimeException("Thời gian lệch so với điểm bắt đầu vượt quá tổng thời gian của tuyến");
        }

        RouteStopId id = new RouteStopId();
        id.setRouteId(route.getId());
        id.setLocationId(location.getId());

        RouteStop routeStop = new RouteStop();
        routeStop.setId(id);
        routeStop.setRoute(route);
        routeStop.setLocation(location);
        routeStop.setStopOrder(requestDTO.getStopOrder());
        routeStop.setTimeOffsetFromStart(requestDTO.getTimeOffsetFromStart());

        RouteStop saved = routeStopRepository.save(routeStop);
        return RouteStopMGMTMapper.toResponseDTO(saved);
    }

    @Override
    public RouteStopMGMTResponseDTO updateRouteStop(RouteStopMGMTRequestDTO requestDTO) {
        Route route = routeRepository.findById(requestDTO.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route không tồn tại"));

        // Kiểm tra timeOffsetFromStart <= defaultDurationMinutes
        if (requestDTO.getTimeOffsetFromStart() > route.getDefaultDurationMinutes()) {
            throw new RuntimeException("Thời gian lệch so với điểm bắt đầu vượt quá tổng thời gian của tuyến");
        }

        RouteStopId id = new RouteStopId();
        id.setRouteId(requestDTO.getRouteId());
        id.setLocationId(requestDTO.getLocationId());

        RouteStop routeStop = routeStopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route stop không tồn tại"));

        routeStop.setStopOrder(requestDTO.getStopOrder());
        routeStop.setTimeOffsetFromStart(requestDTO.getTimeOffsetFromStart());

        RouteStop updated = routeStopRepository.save(routeStop);
        return RouteStopMGMTMapper.toResponseDTO(updated);
    }

    @Override
    public RouteStopDeleteResponseDTO deleteRouteStop(Long routeId, Long locationId, boolean isDelete) {
        RouteStopId id = new RouteStopId();
        id.setRouteId(routeId);
        id.setLocationId(locationId);

        RouteStop routeStop = routeStopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route stop không tồn tại"));

        if (isDelete) {
            routeStopRepository.delete(routeStop);
        }

        return new RouteStopDeleteResponseDTO(routeId, locationId);
    }

    @Override
    public ApiResponse<?> getStopsByRoute(Long routeId) {
        List<RouteStop> stops = routeStopRepository.findByRoute_IdOrderByStopOrderAsc(routeId);
        List<RouteStopMGMTResponseDTO> content = stops.stream()
                .map(RouteStopMGMTMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ApiResponse.success("Lấy danh sách điểm dừng thành công", content);
    }
}
