package com.busify.project.audit_log.controller;

import com.busify.project.audit_log.dto.response.AuditLogPageResponseDTO;
import com.busify.project.audit_log.service.AuditLogService;
import com.busify.project.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
@Tag(name = "Audit Logs", description = "Audit Log Management API")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @Operation(summary = "Get all audit logs with pagination", 
               description = "Retrieve all audit logs sorted by timestamp descending with pagination support")
    @GetMapping
    public ApiResponse<AuditLogPageResponseDTO> getAllAuditLogs(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        try {
            AuditLogPageResponseDTO auditLogs = auditLogService.getAllAuditLogs(page, size);
            return ApiResponse.success("Audit logs retrieved successfully", auditLogs);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Error retrieving audit logs: " + e.getMessage());
        }
    }

    @Operation(summary = "Get audit logs by user ID", 
               description = "Retrieve audit logs for specific user sorted by timestamp descending")
    @GetMapping("/user/{userId}")
    public ApiResponse<AuditLogPageResponseDTO> getAuditLogsByUserId(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        try {
            AuditLogPageResponseDTO auditLogs = auditLogService.getAuditLogsByUserId(userId, page, size);
            return ApiResponse.success("User audit logs retrieved successfully", auditLogs);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Error retrieving user audit logs: " + e.getMessage());
        }
    }

    @Operation(summary = "Get audit logs by action", 
               description = "Retrieve audit logs filtered by specific action")
    @GetMapping("/action/{action}")
    public ApiResponse<AuditLogPageResponseDTO> getAuditLogsByAction(
            @Parameter(description = "Action type", example = "CREATE")
            @PathVariable String action,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        try {
            AuditLogPageResponseDTO auditLogs = auditLogService.getAuditLogsByAction(action, page, size);
            return ApiResponse.success("Action audit logs retrieved successfully", auditLogs);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Error retrieving action audit logs: " + e.getMessage());
        }
    }

    @Operation(summary = "Get audit logs by target entity", 
               description = "Retrieve audit logs filtered by target entity type")
    @GetMapping("/entity/{targetEntity}")
    public ApiResponse<AuditLogPageResponseDTO> getAuditLogsByTargetEntity(
            @Parameter(description = "Target entity type", example = "BOOKING")
            @PathVariable String targetEntity,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        try {
            AuditLogPageResponseDTO auditLogs = auditLogService.getAuditLogsByTargetEntity(targetEntity, page, size);
            return ApiResponse.success("Entity audit logs retrieved successfully", auditLogs);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Error retrieving entity audit logs: " + e.getMessage());
        }
    }

    @Operation(summary = "Get audit logs by date range", 
               description = "Retrieve audit logs within specific date range")
    @GetMapping("/date-range")
    public ApiResponse<AuditLogPageResponseDTO> getAuditLogsByDateRange(
            @Parameter(description = "Start date", example = "2024-01-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date", example = "2024-12-31T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        try {
            AuditLogPageResponseDTO auditLogs = auditLogService.getAuditLogsByDateRange(startDate, endDate, page, size);
            return ApiResponse.success("Date range audit logs retrieved successfully", auditLogs);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Error retrieving date range audit logs: " + e.getMessage());
        }
    }

    @Operation(summary = "Get audit logs with multiple filters", 
               description = "Retrieve audit logs with multiple optional filters")
    @GetMapping("/filter")
    public ApiResponse<AuditLogPageResponseDTO> getAuditLogsByFilters(
            @Parameter(description = "User ID filter", example = "1")
            @RequestParam(required = false) Long userId,
            @Parameter(description = "Action filter", example = "CREATE")
            @RequestParam(required = false) String action,
            @Parameter(description = "Target entity filter", example = "BOOKING")
            @RequestParam(required = false) String targetEntity,
            @Parameter(description = "Start date filter", example = "2024-01-01T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date filter", example = "2024-12-31T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        try {
            AuditLogPageResponseDTO auditLogs = auditLogService.getAuditLogsByFilters(
                userId, action, targetEntity, startDate, endDate, page, size);
            return ApiResponse.success("Filtered audit logs retrieved successfully", auditLogs);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Error retrieving filtered audit logs: " + e.getMessage());
        }
    }
}