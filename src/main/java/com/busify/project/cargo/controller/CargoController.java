package com.busify.project.cargo.controller;

import com.busify.project.cargo.dto.request.CargoCancelRequestDTO;
import com.busify.project.cargo.dto.request.CargoBookingRequestDTO;
import com.busify.project.cargo.dto.request.CargoFeeCalculationRequestDTO;
import com.busify.project.cargo.dto.request.CargoPaymentRequestDTO;
import com.busify.project.cargo.dto.request.CargoQRVerifyRequestDTO;
import com.busify.project.cargo.dto.request.CargoSearchRequestDTO;
import com.busify.project.cargo.dto.request.CargoUpdateStatusRequestDTO;
import com.busify.project.cargo.dto.request.ValidateCargoTripRequestDTO;
import com.busify.project.cargo.dto.response.*;
import com.busify.project.cargo.enums.ImageType;
import com.busify.project.cargo.service.CargoService;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.payment.dto.response.PaymentResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

import java.util.List;

/**
 * CargoController
 * 
 * REST API endpoints for cargo/parcel shipping operations
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-07
 */
@RestController
@RequestMapping("/api/cargo")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cargo", description = "Cargo/Parcel Shipping Management API")
@SecurityRequirement(name = "bearerAuth")
public class CargoController {

        private final CargoService cargoService;

        // ===== CARGO BOOKING OPERATIONS =====

        /**
         * Create new cargo booking
         * Public endpoint - can be used by guests or authenticated users
         */
        @PostMapping
        @Operation(summary = "Create a new cargo booking", description = "Create cargo booking for a specific trip. Can be used by guests or authenticated users.")
        public ApiResponse<CargoBookingResponseDTO> createCargoBooking(
                        @Valid @RequestBody CargoBookingRequestDTO request) {
                CargoBookingResponseDTO response = cargoService.createCargoBooking(request);

                log.info("Cargo booking created successfully: {}", response.getCargoCode());

                return ApiResponse.<CargoBookingResponseDTO>builder()
                                .code(HttpStatus.CREATED.value())
                                .message("Cargo booking created successfully")
                                .result(response)
                                .build();
        }

        /**
         * Create new cargo booking with images (multipart/form-data)
         * Public endpoint - can be used by guests or authenticated users
         */
        @PostMapping(consumes = "multipart/form-data")
        @Operation(summary = "Create cargo booking with images", description = "Create cargo booking with optional cargo images (package photos, labels, etc.)")
        public ApiResponse<CargoBookingResponseDTO> createCargoBookingWithImages(
                        @Valid @ModelAttribute CargoBookingRequestDTO request,
                        @RequestParam(value = "images", required = false) List<MultipartFile> images) {
                CargoBookingResponseDTO response = cargoService.createCargoBooking(request, images);

                log.info("Cargo booking created successfully with {} images: {}",
                                images != null ? images.size() : 0, response.getCargoCode());

                return ApiResponse.<CargoBookingResponseDTO>builder()
                                .code(HttpStatus.CREATED.value())
                                .message("Cargo booking created successfully")
                                .result(response)
                                .build();
        }

        /**
         * Get cargo booking by code (tracking number)
         * Public endpoint - anyone with cargo code can track
         */
        @GetMapping("/{cargoCode}")
        @Operation(summary = "Get cargo booking by code", description = "Retrieve detailed cargo information by cargo code (tracking number)")
        public ApiResponse<CargoDetailResponseDTO> getCargoBooking(
                        @PathVariable @Parameter(description = "Cargo tracking code", example = "CRG-20251107-A1B2") String cargoCode) {
                CargoDetailResponseDTO response = cargoService.getCargoBookingByCode(cargoCode);

                return ApiResponse.<CargoDetailResponseDTO>builder()
                                .code(HttpStatus.OK.value())
                                .message("Cargo booking retrieved successfully")
                                .result(response)
                                .build();
        }

        /**
         * Update cargo status
         * Restricted to STAFF, DRIVER, ADMIN
         */
        @PutMapping("/{cargoCode}/status")
        @PreAuthorize("hasAnyRole('STAFF', 'DRIVER', 'ADMIN')")
        @Operation(summary = "Update cargo status", description = "Update cargo delivery status. Only accessible by staff, driver, or admin.")
        public ApiResponse<CargoBookingResponseDTO> updateCargoStatus(
                        @PathVariable @Parameter(description = "Cargo tracking code") String cargoCode,
                        @Valid @RequestBody CargoUpdateStatusRequestDTO request) {
                CargoBookingResponseDTO response = cargoService.updateCargoStatus(cargoCode, request);

                log.info("Cargo status updated: {} -> {}", cargoCode, request.getStatus());

                return ApiResponse.<CargoBookingResponseDTO>builder()
                                .code(HttpStatus.OK.value())
                                .message("Cargo status updated successfully")
                                .result(response)
                                .build();
        }

        /**
         * Cancel cargo booking
         * Can be cancelled by owner or admin
         */
        @DeleteMapping("/{cargoCode}")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Cancel cargo booking", description = "Cancel cargo booking. Must be at least 24 hours before departure.")
        public ApiResponse<CargoBookingResponseDTO> cancelCargoBooking(
                        @PathVariable @Parameter(description = "Cargo tracking code") String cargoCode,
                        @Valid @RequestBody CargoCancelRequestDTO request) {
                CargoBookingResponseDTO response = cargoService.cancelCargoBooking(cargoCode, request);

                log.info("Cargo booking cancelled: {}", cargoCode);

                return ApiResponse.<CargoBookingResponseDTO>builder()
                                .code(HttpStatus.OK.value())
                                .message("Cargo booking cancelled successfully")
                                .result(response)
                                .build();
        }

        // ===== CARGO LISTING & SEARCH =====

        /**
         * Search cargo with filters
         * Admin/Staff can see all, users see their own
         */
        @GetMapping("/search")
        @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
        @Operation(summary = "Search cargo bookings", description = "Search and filter cargo bookings with pagination. Use tripId, status, cargoType, keyword filters.")
        public ApiResponse<Page<CargoListResponseDTO>> searchCargo(
                        @ModelAttribute CargoSearchRequestDTO searchRequest,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "DESC") String sortDirection) {
                Sort.Direction direction = Sort.Direction.fromString(sortDirection);
                Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

                Page<CargoListResponseDTO> response = cargoService.searchCargo(searchRequest, pageable);

                return ApiResponse.<Page<CargoListResponseDTO>>builder()
                                .code(HttpStatus.OK.value())
                                .message("Cargo bookings retrieved successfully")
                                .result(response)
                                .build();
        }

        /**
         * Get cargo by user ID (my cargo bookings)
         */
        @GetMapping("/my-bookings")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Get my cargo bookings", description = "Get all cargo bookings created by current user")
        public ApiResponse<Page<CargoListResponseDTO>> getMyCargoBookings(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
                Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
                Page<CargoListResponseDTO> response = cargoService.getMyCargoBookings(pageable);

                return ApiResponse.<Page<CargoListResponseDTO>>builder()
                                .code(HttpStatus.OK.value())
                                .message("My cargo bookings retrieved successfully")
                                .result(response)
                                .build();
        }

        // ===== FEE CALCULATION =====

        /**
         * Calculate cargo fee before booking
         * Public endpoint - anyone can calculate fees
         */
        @PostMapping("/calculate-fee")
        @Operation(summary = "Calculate cargo fee", description = "Calculate cargo shipping fee before creating booking")
        public ApiResponse<CargoFeeCalculationResponseDTO> calculateCargoFee(
                        @Valid @RequestBody CargoFeeCalculationRequestDTO request) {
                CargoFeeCalculationResponseDTO response = cargoService.calculateCargoFee(request);

                return ApiResponse.<CargoFeeCalculationResponseDTO>builder()
                                .code(HttpStatus.OK.value())
                                .message("Cargo fee calculated successfully")
                                .result(response)
                                .build();
        }

        // ===== CARGO CAPACITY =====

        /**
         * Get all cargo bookings for a specific trip
         * STAFF/DRIVER can view cargo list for their operator's trips
         */
        @GetMapping("/trip/{tripId}")
        @PreAuthorize("hasAnyRole('STAFF', 'DRIVER', 'ADMIN', 'OPERATOR')")
        @Operation(summary = "Get cargo by trip", description = "Get all cargo bookings for a specific trip with pagination")
        public ApiResponse<Page<CargoListResponseDTO>> getCargoByTrip(
                        @PathVariable @Parameter(description = "Trip ID") Long tripId,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "20") int size,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "DESC") String sortDirection) {
                Sort.Direction direction = Sort.Direction.fromString(sortDirection);
                Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

                Page<CargoListResponseDTO> response = cargoService.getCargoByTrip(tripId, pageable);

                return ApiResponse.<Page<CargoListResponseDTO>>builder()
                                .code(HttpStatus.OK.value())
                                .message("Cargo list for trip retrieved successfully")
                                .result(response)
                                .build();
        }

        /**
         * Get active cargo bookings for a trip (excludes CANCELLED)
         * Useful for DRIVER to see cargo to pickup/deliver
         */
        @GetMapping("/trip/{tripId}/active")
        @PreAuthorize("hasAnyRole('STAFF', 'DRIVER', 'ADMIN', 'OPERATOR')")
        @Operation(summary = "Get active cargo by trip", description = "Get all active cargo bookings for a trip (excludes cancelled)")
        public ApiResponse<List<CargoListResponseDTO>> getActiveCargoByTrip(
                        @PathVariable @Parameter(description = "Trip ID") Long tripId) {
                List<CargoListResponseDTO> response = cargoService.getActiveCargoByTrip(tripId);

                return ApiResponse.<List<CargoListResponseDTO>>builder()
                                .code(HttpStatus.OK.value())
                                .message("Active cargo list for trip retrieved successfully")
                                .result(response)
                                .build();
        }

        /**
         * Get remaining cargo capacity for trip
         */
        @GetMapping("/trip/{tripId}/remaining-capacity")
        @Operation(summary = "Get remaining cargo capacity", description = "Get remaining cargo weight capacity for a trip")
        public ApiResponse<Double> getRemainingCapacity(@PathVariable Long tripId) {
                Double remainingCapacity = cargoService.getRemainingCargoCapacity(tripId);

                return ApiResponse.<Double>builder()
                                .code(HttpStatus.OK.value())
                                .message("Remaining capacity retrieved successfully")
                                .result(remainingCapacity)
                                .build();
        }

        // ===== CARGO TRACKING =====

        /**
         * Get tracking history for cargo
         * Public endpoint - anyone with cargo code can track
         */
        @GetMapping("/{cargoCode}/tracking")
        @Operation(summary = "Get cargo tracking history", description = "Get complete tracking history for cargo shipment")
        public ApiResponse<List<CargoTrackingResponseDTO>> getTrackingHistory(
                        @PathVariable @Parameter(description = "Cargo tracking code") String cargoCode) {
                List<CargoTrackingResponseDTO> response = cargoService.getTrackingHistory(cargoCode);

                return ApiResponse.<List<CargoTrackingResponseDTO>>builder()
                                .code(HttpStatus.OK.value())
                                .message("Tracking history retrieved successfully")
                                .result(response)
                                .build();
        }

        // ===== CARGO IMAGES =====

        /**
         * Upload cargo image
         */
        @PostMapping("/{cargoCode}/images")
        @PreAuthorize("hasAnyRole('STAFF', 'DRIVER', 'ADMIN', 'CUSTOMER')")
        @Operation(summary = "Upload cargo image", description = "Upload image for cargo (pickup, delivery, damage proof)")
        public ApiResponse<String> uploadCargoImage(
                        @PathVariable String cargoCode,
                        @RequestParam("image") MultipartFile imageFile,
                        @RequestParam ImageType imageType,
                        @RequestParam(required = false) String description) {
                String imageUrl = cargoService.uploadCargoImage(cargoCode, imageFile, imageType, description);

                return ApiResponse.<String>builder()
                                .code(HttpStatus.CREATED.value())
                                .message("Image uploaded successfully")
                                .result(imageUrl)
                                .build();
        }

        /**
         * Get cargo images
         */
        @GetMapping("/{cargoCode}/images")
        @Operation(summary = "Get cargo images", description = "Get all images for cargo booking")
        public ApiResponse<List<CargoDetailResponseDTO.ImageInfo>> getCargoImages(
                        @PathVariable String cargoCode) {
                List<CargoDetailResponseDTO.ImageInfo> response = cargoService.getCargoImages(cargoCode);

                return ApiResponse.<List<CargoDetailResponseDTO.ImageInfo>>builder()
                                .code(HttpStatus.OK.value())
                                .message("Cargo images retrieved successfully")
                                .result(response)
                                .build();
        }

        /**
         * Delete cargo image
         */
        @DeleteMapping("/{cargoCode}/images/{imageId}")
        @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
        @Operation(summary = "Delete cargo image", description = "Delete cargo image by ID")
        public ApiResponse<Void> deleteCargoImage(
                        @PathVariable String cargoCode,
                        @PathVariable Long imageId) {
                cargoService.deleteCargoImage(cargoCode, imageId);

                return ApiResponse.<Void>builder()
                                .code(HttpStatus.OK.value())
                                .message("Image deleted successfully")
                                .build();
        }

        // ===== PAYMENT OPERATIONS =====

        /**
         * Create cargo payment
         */
        @PostMapping("/{cargoCode}/payment")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Create cargo payment", description = "Initiate payment for cargo booking (VNPay, PayPal, CreditCard)")
        public ApiResponse<PaymentResponseDTO> createCargoPayment(
                        @PathVariable String cargoCode,
                        @Valid @RequestBody CargoPaymentRequestDTO request) {
                PaymentResponseDTO response = cargoService.createCargoPayment(request);

                log.info("Cargo payment created for: {}", cargoCode);

                return ApiResponse.<PaymentResponseDTO>builder()
                                .code(HttpStatus.CREATED.value())
                                .message("Payment created successfully. Redirect to payment URL.")
                                .result(response)
                                .build();
        }

        /**
         * PayPal success callback for cargo
         */
        @GetMapping("/{cargoCode}/payment/success")
        @Operation(summary = "PayPal cargo payment success callback", description = "Handle successful PayPal payment for cargo")
        public ApiResponse<PaymentResponseDTO> cargoPaymentSuccess(
                        @PathVariable String cargoCode,
                        @RequestParam("paymentId") String paypalPaymentId,
                        @RequestParam("PayerID") String payerId) {
                log.info("PayPal cargo payment success - Cargo: {}, PayPal ID: {}", cargoCode, paypalPaymentId);

                PaymentResponseDTO response = cargoService.executeCargoPayment(cargoCode, paypalPaymentId, payerId);

                if (response.getStatus().name().equals("completed")) {
                        return ApiResponse.<PaymentResponseDTO>builder()
                                        .code(HttpStatus.OK.value())
                                        .message("Cargo payment completed successfully")
                                        .result(response)
                                        .build();
                } else {
                        return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Payment not completed");
                }
        }

        /**
         * PayPal cancel callback for cargo
         */
        @GetMapping("/{cargoCode}/payment/cancel")
        @Operation(summary = "PayPal cargo payment cancel callback", description = "Handle cancelled PayPal payment for cargo")
        public ApiResponse<Void> cargoPaymentCancel(@PathVariable String cargoCode) {
                log.warn("PayPal cargo payment cancelled - Cargo: {}", cargoCode);

                return ApiResponse.<Void>builder()
                                .code(HttpStatus.OK.value())
                                .message("Payment cancelled by user")
                                .build();
        }

        // ===== STATISTICS & ADMIN =====

        /**
         * Get cargo statistics
         */
        @GetMapping("/statistics")
        @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
        @Operation(summary = "Get cargo statistics", description = "Get cargo booking statistics by status")
        public ApiResponse<CargoStatisticsResponseDTO> getCargoStatistics(
                        @RequestParam(required = false) Long operatorId) {
                CargoStatisticsResponseDTO response = cargoService.getCargoStatistics(operatorId);

                return ApiResponse.<CargoStatisticsResponseDTO>builder()
                                .code(HttpStatus.OK.value())
                                .message("Statistics retrieved successfully")
                                .result(response)
                                .build();
        }

        /**
         * Get cargo revenue by trip
         */
        @GetMapping("/trip/{tripId}/revenue")
        @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
        @Operation(summary = "Get cargo revenue by trip", description = "Get total cargo revenue for a specific trip")
        public ApiResponse<Double> getCargoRevenue(@PathVariable Long tripId) {
                Double revenue = cargoService.getCargoRevenueByTrip(tripId);

                return ApiResponse.<Double>builder()
                                .code(HttpStatus.OK.value())
                                .message("Cargo revenue retrieved successfully")
                                .result(revenue)
                                .build();
        }

        /**
         * Update internal notes (admin only)
         */
        @PutMapping("/{cargoCode}/notes")
        @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
        @Operation(summary = "Update internal notes", description = "Update internal notes for cargo (admin/staff only)")
        public ApiResponse<Void> updateInternalNotes(
                        @PathVariable String cargoCode,
                        @RequestParam String notes) {
                cargoService.updateInternalNotes(cargoCode, notes);

                return ApiResponse.<Void>builder()
                                .code(HttpStatus.OK.value())
                                .message("Internal notes updated successfully")
                                .build();
        }

        // ===== VALIDATION =====

        /**
         * Validate cargo and trip (POST)
         * Used by driver/staff to verify cargo belongs to trip before pickup/delivery
         */
        @PostMapping("/validate-cargo-trip")
        @Operation(summary = "Validate cargo and trip", description = "Validate that cargo belongs to the specified trip. Used by driver/staff for cargo verification.")
        public ApiResponse<CargoValidationResponseDTO> validateCargoTrip(
                        @Valid @RequestBody ValidateCargoTripRequestDTO request) {
                try {
                        CargoValidationResponseDTO response = cargoService.validateCargoTrip(
                                        request.getTripId(),
                                        request.getCargoCode());

                        String message = response.getIsValid()
                                        ? "Xác thực cargo và trip thành công"
                                        : "Cargo không hợp lệ: " + response.getValidationMessage();

                        return ApiResponse.<CargoValidationResponseDTO>builder()
                                        .code(response.getIsValid() ? HttpStatus.OK.value()
                                                        : HttpStatus.BAD_REQUEST.value())
                                        .message(message)
                                        .result(response)
                                        .build();
                } catch (IllegalArgumentException e) {
                        return ApiResponse.<CargoValidationResponseDTO>builder()
                                        .code(HttpStatus.BAD_REQUEST.value())
                                        .message(e.getMessage())
                                        .build();
                } catch (Exception e) {
                        log.error("Error validating cargo trip: ", e);
                        return ApiResponse.<CargoValidationResponseDTO>builder()
                                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                        .message("Lỗi khi xác thực cargo và trip: " + e.getMessage())
                                        .build();
                }
        }

        /**
         * Validate cargo and trip (GET - query params)
         * Used by driver/staff to verify cargo belongs to trip before pickup/delivery
         */
        @GetMapping("/validate-cargo-trip")
        @Operation(summary = "Validate cargo and trip (query params)", description = "Validate that cargo belongs to the specified trip using query parameters.")
        public ApiResponse<CargoValidationResponseDTO> validateCargoTripByParams(
                        @RequestParam Long tripId,
                        @RequestParam String cargoCode) {
                try {
                        CargoValidationResponseDTO response = cargoService.validateCargoTrip(tripId, cargoCode);

                        String message = response.getIsValid()
                                        ? "Xác thực cargo và trip thành công"
                                        : "Cargo không hợp lệ: " + response.getValidationMessage();

                        return ApiResponse.<CargoValidationResponseDTO>builder()
                                        .code(response.getIsValid() ? HttpStatus.OK.value()
                                                        : HttpStatus.BAD_REQUEST.value())
                                        .message(message)
                                        .result(response)
                                        .build();
                } catch (IllegalArgumentException e) {
                        return ApiResponse.<CargoValidationResponseDTO>builder()
                                        .code(HttpStatus.BAD_REQUEST.value())
                                        .message(e.getMessage())
                                        .build();
                } catch (Exception e) {
                        log.error("Error validating cargo trip: ", e);
                        return ApiResponse.<CargoValidationResponseDTO>builder()
                                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                        .message("Lỗi khi xác thực cargo và trip: " + e.getMessage())
                                        .build();
                }
        }

        // ===== QR CODE VERIFICATION =====

        /**
         * Verify QR code and confirm cargo pickup
         * Staff scans QR code from receiver's email to confirm delivery
         * 
         * POST /api/cargo/verify-qr-pickup
         */
        @PostMapping("/verify-qr-pickup")
        @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
        @Operation(summary = "Verify QR code for cargo pickup", description = "Staff verifies QR code and confirms cargo delivery to receiver", security = @SecurityRequirement(name = "bearerAuth"))
        public ApiResponse<CargoQRVerifyResponseDTO> verifyQRPickup(
                        @Valid @RequestBody CargoQRVerifyRequestDTO request) {

                log.info("Verifying QR code for cargo pickup");

                CargoQRVerifyResponseDTO response = cargoService.confirmCargoPickupByQR(request);

                return ApiResponse.<CargoQRVerifyResponseDTO>builder()
                                .code(HttpStatus.OK.value())
                                .message("Xác thực QR thành công - Đã giao hàng")
                                .result(response)
                                .build();
        }

        // ===== HELPER METHODS =====
        // No longer needed - UserService handles authentication in Service layer
}