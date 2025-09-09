package com.busify.project.route.service.impl;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.location.entity.Location;
import com.busify.project.location.repository.LocationRepository;
import com.busify.project.route.dto.request.RouteMGMTRequestDTO;
import com.busify.project.route.dto.response.RouteDeleteResponseDTO;
import com.busify.project.route.dto.response.RouteMGMTResposeDTO;
import com.busify.project.route.entity.Route;
import com.busify.project.route.mapper.RouteMGMTMapper;
import com.busify.project.route.repository.RouteRepository;
import com.busify.project.route.service.RouteMGMTService;
import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.audit_log.service.AuditLogService;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteMGMTServiceImpl implements RouteMGMTService {

    private final RouteRepository routeRepository;
    private final LocationRepository locationRepository;
    private final AuditLogService auditLogService;
    private final UserRepository userRepository;

    @Override
    public RouteMGMTResposeDTO addRoute(RouteMGMTRequestDTO requestDTO) {
        Route route = new Route();

        Location startLocation = locationRepository.findById(requestDTO.getStartLocationId())
                .orElseThrow(() -> new RuntimeException("Start location không tồn tại"));
        route.setStartLocation(startLocation);

        Location endLocation = locationRepository.findById(requestDTO.getEndLocationId())
                .orElseThrow(() -> new RuntimeException("End location không tồn tại"));
        route.setEndLocation(endLocation);

        // Check 2 location không được trùng nhau
        if (startLocation.getId().equals(endLocation.getId())) {
            throw new RuntimeException("Start location và End location không được trùng nhau");
        }

        // Tạo tên tự động từ start và end location
        String routeName = startLocation.getName() + " ⟶ " + endLocation.getName();
        route.setName(routeName);

        route.setDefaultDurationMinutes(requestDTO.getDefaultDurationMinutes());
        route.setDefaultPrice(requestDTO.getDefaultPrice());

        Route saved = routeRepository.save(route);

        // Audit log for route creation
        try {
            User currentUser = getCurrentUser();
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("CREATE");
            auditLog.setTargetEntity("ROUTE");
            auditLog.setTargetId(saved.getId());
            auditLog.setDetails(String.format("{\"route_id\":%d,\"route_name\":\"%s\",\"start_location_id\":%d,\"end_location_id\":%d,\"default_price\":%.2f,\"default_duration\":%d,\"action\":\"create\"}", 
                    saved.getId(), saved.getName(), startLocation.getId(), endLocation.getId(), 
                    saved.getDefaultPrice(), saved.getDefaultDurationMinutes()));
            auditLog.setUser(currentUser);
            auditLogService.save(auditLog);
        } catch (Exception e) {
            System.err.println("Failed to create audit log for route creation: " + e.getMessage());
        }

        return RouteMGMTMapper.toRouteDetailResponseDTO(saved);
    }

    @Override
    public RouteMGMTResposeDTO updateRoute(Long id, RouteMGMTRequestDTO requestDTO) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route không tồn tại"));

        Location startLocation = null;
        Location endLocation = null;

        if (requestDTO.getStartLocationId() != null) {
            startLocation = locationRepository.findById(requestDTO.getStartLocationId())
                    .orElseThrow(() -> new RuntimeException("Start location không tồn tại"));
            route.setStartLocation(startLocation);
        } else {
            startLocation = route.getStartLocation();
        }

        if (requestDTO.getEndLocationId() != null) {
            endLocation = locationRepository.findById(requestDTO.getEndLocationId())
                    .orElseThrow(() -> new RuntimeException("End location không tồn tại"));
            route.setEndLocation(endLocation);
        } else {
            endLocation = route.getEndLocation();
        }

        // Check 2 location không được trùng nhau (kể cả khi update chỉ sửa 1 bên)
        if (startLocation != null && endLocation != null
                && startLocation.getId().equals(endLocation.getId())) {
            throw new RuntimeException("Start location và End location không được trùng nhau");
        }

        // Tạo tên tự động từ start và end location (ensure both are not null)
        if (startLocation != null && endLocation != null) {
            String routeName = startLocation.getName() + " ⟶ " + endLocation.getName();
            route.setName(routeName);
        }

        if (requestDTO.getDefaultDurationMinutes() != null) {
            route.setDefaultDurationMinutes(requestDTO.getDefaultDurationMinutes());
        }

        if (requestDTO.getDefaultPrice() != null) {
            route.setDefaultPrice(requestDTO.getDefaultPrice());
        }

        Route updated = routeRepository.save(route);

        // Audit log for route update
        try {
            User currentUser = getCurrentUser();
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("UPDATE");
            auditLog.setTargetEntity("ROUTE");
            auditLog.setTargetId(updated.getId());
            // Use the actual locations from the updated route entity
            auditLog.setDetails(String.format("{\"route_id\":%d,\"route_name\":\"%s\",\"start_location_id\":%d,\"end_location_id\":%d,\"default_price\":%.2f,\"default_duration\":%d,\"action\":\"update\"}", 
                    updated.getId(), updated.getName(), updated.getStartLocation().getId(), updated.getEndLocation().getId(), 
                    updated.getDefaultPrice(), updated.getDefaultDurationMinutes()));
            auditLog.setUser(currentUser);
            auditLogService.save(auditLog);
        } catch (Exception e) {
            System.err.println("Failed to create audit log for route update: " + e.getMessage());
        }

        return RouteMGMTMapper.toRouteDetailResponseDTO(updated);
    }

    @Override
    public RouteDeleteResponseDTO deleteRoute(Long id, boolean isDelete) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route không tồn tại"));

        if (isDelete) {
            // Audit log for route deletion (before deletion)
            try {
                User currentUser = getCurrentUser();
                AuditLog auditLog = new AuditLog();
                auditLog.setAction("DELETE");
                auditLog.setTargetEntity("ROUTE");
                auditLog.setTargetId(route.getId());
                auditLog.setDetails(String.format("{\"route_id\":%d,\"route_name\":\"%s\",\"start_location_id\":%d,\"end_location_id\":%d,\"action\":\"hard_delete\"}", 
                        route.getId(), route.getName(), route.getStartLocation().getId(), route.getEndLocation().getId()));
                auditLog.setUser(currentUser);
                auditLogService.save(auditLog);
            } catch (Exception e) {
                System.err.println("Failed to create audit log for route deletion: " + e.getMessage());
            }

            routeRepository.delete(route);
        }

        return new RouteDeleteResponseDTO(
                route.getId(),
                route.getName()
        );
    }

    @Override
    public ApiResponse<?> getAllRoutes(String keyword, int page, int size) {
        Page<Route> routePage = routeRepository.searchRoutes(keyword, PageRequest.of(page - 1, size));

        List<RouteMGMTResposeDTO> content = routePage.stream()
                .map(RouteMGMTMapper::toRouteDetailResponseDTO)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("result", content);
        response.put("pageNumber", routePage.getNumber() + 1);
        response.put("pageSize", routePage.getSize());
        response.put("totalRecords", routePage.getTotalElements());
        response.put("totalPages", routePage.getTotalPages());
        response.put("hasNext", routePage.hasNext());
        response.put("hasPrevious", routePage.hasPrevious());

        return ApiResponse.success("Lấy danh sách tuyến đường thành công", response);
    }

    // Helper method to get current user from SecurityContext
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("No authenticated user found");
        }
        
        String email = authentication.getName();
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}
