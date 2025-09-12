package com.busify.project.refund.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.refund.dto.request.RefundRequestDTO;
import com.busify.project.refund.dto.response.RefundResponseDTO;
import com.busify.project.refund.service.RefundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
@Slf4j
public class RefundController {

    private final RefundService refundService;

    /**
     * Tạo yêu cầu refund mới
     */
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

    /**
     * Xử lý refund (dành cho admin/operator)
     */
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

    /**
     * Lấy thông tin refund theo ID
     */
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

    /**
     * Lấy danh sách refund theo payment ID
     */
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

    /**
     * Lấy danh sách refund theo customer ID
     */
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

    /**
     * Kiểm tra trạng thái refund từ payment gateway
     */
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

    /**
     * Hủy yêu cầu refund
     */
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

    /**
     * Lấy tất cả refund với phân trang (dành cho admin)
     */
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
