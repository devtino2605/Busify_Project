package com.busify.project.refund.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.refund.dto.request.RefundRequestDTO;
import com.busify.project.refund.dto.response.RefundResponseDTO;
import com.busify.project.refund.service.RefundService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Refunds", description = "Refund Management API")
public class RefundController {

    private final RefundService refundService;

    @Operation(summary = "Create refund request", description = "Create a new refund request")
    @PostMapping("/create")
    public ApiResponse<RefundResponseDTO> createRefund(@Valid @RequestBody RefundRequestDTO refundRequest) {
        try {
            RefundResponseDTO result = refundService.createRefund(refundRequest);
            return ApiResponse.<RefundResponseDTO>builder()
                    .code(HttpStatus.CREATED.value())
                    .message("Refund request created successfully")
                    .result(result)
                    .build();
        } catch (Exception e) {
            log.error("Error creating refund request", e);
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Process refund request", description = "Process refund request by admin or operator")
    @PostMapping("/{refundId}/process")
    public ApiResponse<RefundResponseDTO> processRefund(@PathVariable Long refundId) {
        try {
            RefundResponseDTO result = refundService.processRefund(refundId);
            return ApiResponse.<RefundResponseDTO>builder()
                    .code(HttpStatus.OK.value())
                    .message("Refund processed successfully")
                    .result(result)
                    .build();
        } catch (Exception e) {
            log.error("Error processing refund ID: {}", refundId, e);
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get refund request information", description = "Get detailed information of a refund request by ID")
    @GetMapping("/{refundId}")
    public ApiResponse<RefundResponseDTO> getRefundById(@PathVariable Long refundId) {
        try {
            RefundResponseDTO result = refundService.getRefundById(refundId);
            return ApiResponse.<RefundResponseDTO>builder()
                    .code(HttpStatus.OK.value())
                    .message("Refund retrieved successfully")
                    .result(result)
                    .build();
        } catch (Exception e) {
            log.error("Error retrieving refund ID: {}", refundId, e);
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get refunds by payment ID", description = "Get list of refund requests related to a payment ID")
    @GetMapping("/payment/{paymentId}")
    public ApiResponse<List<RefundResponseDTO>> getRefundsByPaymentId(@PathVariable Long paymentId) {
        try {
            List<RefundResponseDTO> results = refundService.getRefundsByPaymentId(paymentId);
            return ApiResponse.<List<RefundResponseDTO>>builder()
                    .code(HttpStatus.OK.value())
                    .message("Refunds retrieved successfully")
                    .result(results)
                    .build();
        } catch (Exception e) {
            log.error("Error retrieving refunds for payment ID: {}", paymentId, e);
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get refunds by customer ID", description = "Get list of refund requests of a customer by ID")
    @GetMapping("/customer/{customerId}")
    public ApiResponse<List<RefundResponseDTO>> getRefundsByCustomerId(@PathVariable Long customerId) {
        try {
            List<RefundResponseDTO> results = refundService.getRefundsByCustomerId(customerId);
            return ApiResponse.<List<RefundResponseDTO>>builder()
                    .code(HttpStatus.OK.value())
                    .message("Customer refunds retrieved successfully")
                    .result(results)
                    .build();
        } catch (Exception e) {
            log.error("Error retrieving refunds for customer ID: {}", customerId, e);
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Check refund status", description = "Check status of a refund request from payment gateway")
    @GetMapping("/{refundId}/status")
    public ApiResponse<RefundResponseDTO> checkRefundStatus(@PathVariable Long refundId) {
        try {
            RefundResponseDTO result = refundService.checkRefundStatus(refundId);
            return ApiResponse.<RefundResponseDTO>builder()
                    .code(HttpStatus.OK.value())
                    .message("Refund status checked successfully")
                    .result(result)
                    .build();
        } catch (Exception e) {
            log.error("Error checking refund status for ID: {}", refundId, e);
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Cancel refund request", description = "Cancel a created refund request")
    @PostMapping("/{refundId}/cancel")
    public ApiResponse<RefundResponseDTO> cancelRefund(@PathVariable Long refundId) {
        try {
            RefundResponseDTO result = refundService.cancelRefund(refundId);
            return ApiResponse.<RefundResponseDTO>builder()
                    .code(HttpStatus.OK.value())
                    .message("Refund cancelled successfully")
                    .result(result)
                    .build();
        } catch (Exception e) {
            log.error("Error cancelling refund ID: {}", refundId, e);
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get all refund requests", description = "Get list of all refund requests with pagination (for admin)")
    @GetMapping("/all")
    public ApiResponse<List<RefundResponseDTO>> getAllRefunds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<RefundResponseDTO> results = refundService.getAllRefunds(page, size);
            return ApiResponse.<List<RefundResponseDTO>>builder()
                    .code(HttpStatus.OK.value())
                    .message("All refunds retrieved successfully")
                    .result(results)
                    .build();
        } catch (Exception e) {
            log.error("Error retrieving all refunds", e);
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}