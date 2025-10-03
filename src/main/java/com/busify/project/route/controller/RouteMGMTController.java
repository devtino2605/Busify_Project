package com.busify.project.route.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.route.dto.request.RouteMGMTRequestDTO;
import com.busify.project.route.dto.response.RouteDeleteResponseDTO;
import com.busify.project.route.dto.response.RouteMGMTResposeDTO;
import com.busify.project.route.service.RouteMGMTService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/route-management")
@RequiredArgsConstructor
@Tag(name = "Route Management", description = "Route Management API")
public class RouteMGMTController {

    private final RouteMGMTService routeMGMTService;

    @Operation(summary = "Get all routes")
    @GetMapping
    public ApiResponse<?> getAllRoutes(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return routeMGMTService.getAllRoutes(keyword, page, size);
    }

    @Operation(summary = "Add new route")
    @PostMapping
    public ApiResponse<RouteMGMTResposeDTO> addRoute(@Valid @RequestBody RouteMGMTRequestDTO requestDTO) {
        return ApiResponse.success("Thêm mới tuyến đường thành công", routeMGMTService.addRoute(requestDTO));
    }

    @Operation(summary = "Update route")
    @PutMapping("/{id}")
    public ApiResponse<RouteMGMTResposeDTO> updateRoute(
            @PathVariable Long id,
            @Valid @RequestBody RouteMGMTRequestDTO requestDTO
    ) {
        return ApiResponse.success("Cập nhật tuyến đường thành công", routeMGMTService.updateRoute(id, requestDTO));
    }

    @Operation(summary = "Delete route")
    @DeleteMapping("/{id}")
    public ApiResponse<RouteDeleteResponseDTO> deleteRoute(
            @PathVariable Long id,
            @RequestParam boolean isDelete
    ) {
        String message = isDelete
                ? "Xóa tuyến đường thành công"
                : "Bạn đã xác nhận không xóa tuyến đường";

        return ApiResponse.success(message, routeMGMTService.deleteRoute(id, isDelete));
    }
}