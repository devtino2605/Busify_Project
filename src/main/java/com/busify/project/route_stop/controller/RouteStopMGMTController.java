package com.busify.project.route_stop.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.route_stop.dto.request.RouteStopMGMTRequestDTO;
import com.busify.project.route_stop.dto.response.RouteStopDeleteResponseDTO;
import com.busify.project.route_stop.dto.response.RouteStopMGMTResponseDTO;
import com.busify.project.route_stop.service.RouteStopMGMTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/route-stop-management")
@RequiredArgsConstructor
@Tag(name = "Route Stop Management", description = "Route Stop Management API")
public class RouteStopMGMTController {

    private final RouteStopMGMTService routeStopMGMTService;

    @Operation(summary = "Get all stops of a route")
    @GetMapping("/{routeId}")
    public ApiResponse<?> getStopsByRoute(@PathVariable Long routeId) {
        return routeStopMGMTService.getStopsByRoute(routeId);
    }

    @Operation(summary = "Add new route stop")
    @PostMapping
    public ApiResponse<RouteStopMGMTResponseDTO> addRouteStop(
            @Valid @RequestBody RouteStopMGMTRequestDTO requestDTO) {
        return ApiResponse.success("Thêm mới điểm dừng thành công",
                routeStopMGMTService.addRouteStop(requestDTO));
    }

    @Operation(summary = "Update route stop")
    @PutMapping
    public ApiResponse<RouteStopMGMTResponseDTO> updateRouteStop(
            @Valid @RequestBody RouteStopMGMTRequestDTO requestDTO) {
        return ApiResponse.success("Cập nhật điểm dừng thành công",
                routeStopMGMTService.updateRouteStop(requestDTO));
    }

    @Operation(summary = "Delete route stop")
    @DeleteMapping("/{routeId}/{locationId}")
    public ApiResponse<RouteStopDeleteResponseDTO> deleteRouteStop(
            @PathVariable Long routeId,
            @PathVariable Long locationId,
            @RequestParam boolean isDelete) {
        String message = isDelete
                ? "Xóa điểm dừng thành công"
                : "Bạn đã xác nhận không xóa điểm dừng";
        return ApiResponse.success(message,
                routeStopMGMTService.deleteRouteStop(routeId, locationId, isDelete));
    }
}
