package com.busify.project.cargo.service;

import com.busify.project.cargo.dto.request.CargoCancelRequestDTO;
import com.busify.project.cargo.dto.request.CargoBookingRequestDTO;
import com.busify.project.cargo.dto.request.CargoFeeCalculationRequestDTO;
import com.busify.project.cargo.dto.request.CargoPaymentRequestDTO;
import com.busify.project.cargo.dto.request.CargoQRVerifyRequestDTO;
import com.busify.project.cargo.dto.request.CargoSearchRequestDTO;
import com.busify.project.cargo.dto.request.CargoUpdateStatusRequestDTO;
import com.busify.project.cargo.dto.response.*;
import com.busify.project.cargo.enums.ImageType;
import com.busify.project.cargo.exception.CargoBookingException;
import com.busify.project.cargo.exception.CargoNotFoundException;
import com.busify.project.cargo.exception.CargoWeightExceededException;
import com.busify.project.cargo.exception.InvalidCargoStatusTransitionException;
import com.busify.project.payment.dto.response.PaymentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * CargoService Interface
 * 
 * Service interface for cargo booking business logic
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
public interface CargoService {

        // ===== CORE CARGO BOOKING OPERATIONS =====

        /**
         * Create a new cargo booking
         * Uses current authenticated user from UserService (nullable for guest users)
         * 
         * @param request Cargo booking request data
         * @return Created cargo booking response
         * @throws CargoBookingException        if validation fails
         * @throws CargoWeightExceededException if cargo weight exceeds trip capacity
         */
        CargoBookingResponseDTO createCargoBooking(CargoBookingRequestDTO request);

        /**
         * Create a new cargo booking with images
         * Uses current authenticated user from UserService (nullable for guest users)
         * 
         * @param request Cargo booking request data
         * @param images  List of cargo images (optional)
         * @return Created cargo booking response
         * @throws CargoBookingException        if validation fails
         * @throws CargoWeightExceededException if cargo weight exceeds trip capacity
         */
        CargoBookingResponseDTO createCargoBooking(CargoBookingRequestDTO request, List<MultipartFile> images);

        /**
         * Get cargo booking details by cargo code
         * 
         * @param cargoCode Unique cargo code
         * @return Detailed cargo booking information
         * @throws CargoNotFoundException if cargo not found
         */
        CargoDetailResponseDTO getCargoBookingByCode(String cargoCode);

        /**
         * Update cargo status with tracking
         * Uses current authenticated user from UserService
         * 
         * @param cargoCode Unique cargo code
         * @param request   Status update request with notes
         * @return Updated cargo booking response
         * @throws CargoNotFoundException                if cargo not found
         * @throws InvalidCargoStatusTransitionException if status transition is invalid
         */
        CargoBookingResponseDTO updateCargoStatus(String cargoCode, CargoUpdateStatusRequestDTO request);

        /**
         * Cancel cargo booking
         * Uses current authenticated user from UserService
         * 
         * @param cargoCode Unique cargo code
         * @param request   Cancel request with reason
         * @return Updated cargo booking response
         * @throws CargoNotFoundException if cargo not found
         * @throws CargoBookingException  if cargo cannot be cancelled
         */
        CargoBookingResponseDTO cancelCargoBooking(String cargoCode, CargoCancelRequestDTO request);

        // ===== CARGO SEARCH AND LISTING =====

        /**
         * Get all cargo bookings by user ID (for admin use)
         * 
         * @param userId   User ID
         * @param pageable Pagination parameters
         * @return Page of cargo bookings
         */
        Page<CargoListResponseDTO> getCargoByUserId(Long userId, Pageable pageable);

        /**
         * Get all cargo bookings for current authenticated user
         * Uses UserService to get current user ID
         * 
         * @param pageable Pagination parameters
         * @return Page of cargo bookings for current user
         */
        Page<CargoListResponseDTO> getMyCargoBookings(Pageable pageable);

        /**
         * Search cargo bookings with filters
         * Supports filtering by tripId, status, cargoType, keyword, etc.
         * 
         * @param request  Search request with multiple filter criteria
         * @param pageable Pagination parameters
         * @return Page of cargo bookings matching criteria
         */
        Page<CargoListResponseDTO> searchCargo(CargoSearchRequestDTO request, Pageable pageable);

        /**
         * Get all cargo bookings for a specific trip
         * 
         * @param tripId   Trip ID
         * @param pageable Pagination parameters
         * @return Page of cargo bookings for the trip
         */
        Page<CargoListResponseDTO> getCargoByTrip(Long tripId, Pageable pageable);

        /**
         * Get active cargo bookings for a specific trip (excludes CANCELLED)
         * 
         * @param tripId Trip ID
         * @return List of active cargo bookings
         */
        List<CargoListResponseDTO> getActiveCargoByTrip(Long tripId);

        // ===== FEE CALCULATION =====

        /**
         * Calculate cargo fee before booking
         * 
         * @param request Cargo fee calculation request (lightweight DTO)
         * @return Detailed fee calculation breakdown
         */
        CargoFeeCalculationResponseDTO calculateCargoFee(CargoFeeCalculationRequestDTO request);

        // ===== CARGO CAPACITY VALIDATION =====

        /**
         * Get current cargo weight for a trip
         * 
         * @param tripId Trip ID
         * @return Total weight of all cargo on trip (in kg)
         */
        Double getCurrentCargoWeight(Long tripId);

        /**
         * Get remaining cargo capacity for a trip
         * 
         * @param tripId Trip ID
         * @return Remaining weight capacity (in kg)
         */
        Double getRemainingCargoCapacity(Long tripId);

        // ===== CARGO TRACKING =====

        /**
         * Get tracking history for a cargo booking
         * 
         * @param cargoCode Unique cargo code
         * @return List of tracking records ordered by timestamp
         * @throws CargoNotFoundException if cargo not found
         */
        List<CargoTrackingResponseDTO> getTrackingHistory(String cargoCode);

        // ===== CARGO IMAGES =====

        /**
         * Upload cargo image
         * 
         * @param cargoCode   Unique cargo code
         * @param imageFile   Image file to upload
         * @param imageType   Type of image (PACKAGE, DELIVERY_PROOF, etc.)
         * @param description Optional image description
         * @return URL of uploaded image
         * @throws CargoNotFoundException if cargo not found
         */
        String uploadCargoImage(String cargoCode, MultipartFile imageFile, ImageType imageType, String description);

        /**
         * Get all images for a cargo booking
         * 
         * @param cargoCode Unique cargo code
         * @return List of image URLs with types
         * @throws CargoNotFoundException if cargo not found
         */
        List<CargoDetailResponseDTO.ImageInfo> getCargoImages(String cargoCode);

        /**
         * Delete cargo image
         * 
         * @param cargoCode Unique cargo code
         * @param imageId   Image ID to delete
         * @throws CargoNotFoundException if cargo not found
         */
        void deleteCargoImage(String cargoCode, Long imageId);

        // ===== STATISTICS & REPORTING =====

        /**
         * Get cargo statistics for operator dashboard
         * 
         * @param operatorId Operator ID
         * @return Cargo statistics (count by status, total revenue, etc.)
         */
        CargoStatisticsResponseDTO getCargoStatistics(Long operatorId);

        /**
         * Get cargo revenue for a specific trip
         * 
         * @param tripId Trip ID
         * @return Total cargo revenue for the trip
         */
        Double getCargoRevenueByTrip(Long tripId);

        // ===== ADMIN OPERATIONS =====

        /**
         * Auto-cancel cargo bookings when trip is cancelled
         * Full refund (100%) is applied because the cancellation is not customer's
         * fault
         * 
         * @param tripId             Trip ID
         * @param cancellationReason Reason from trip cancellation (displayed to
         *                           customer)
         * @return Number of cargo bookings cancelled
         */
        int autoCancelCargoByTrip(Long tripId, String cancellationReason);

        /**
         * Get customer emails for active cargo bookings of a trip
         * Used for sending notifications when trip is cancelled/delayed
         * 
         * @param tripId Trip ID
         * @return List of unique customer emails (sender emails)
         */
        List<String> getCargoCustomerEmailsByTripId(Long tripId);

        /**
         * Auto-process cargo bookings when trip departs
         * - Cargo with PICKED_UP status → IN_TRANSIT
         * - Cargo with PENDING or CONFIRMED status (not picked up) → CANCELLED with
         * email notification
         * 
         * @param tripId Trip ID
         * @return Map with counts: {inTransit: X, cancelled: Y}
         */
        Map<String, Integer> autoProcessCargoWhenTripDeparted(Long tripId);

        /**
         * Auto-process cargo bookings when trip arrives at destination
         * - Cargo with IN_TRANSIT status → ARRIVED (ready for delivery/pickup)
         * - Sends arrival notification email to receiver
         * 
         * @param tripId Trip ID
         * @return Map with counts: {arrived: X}
         */
        Map<String, Integer> autoProcessCargoWhenTripArrived(Long tripId);

        /**
         * Update internal notes (staff only)
         * Uses current authenticated user from UserService
         * 
         * @param cargoCode     Unique cargo code
         * @param internalNotes Internal notes (not visible to customer)
         * @throws CargoNotFoundException if cargo not found
         */
        void updateInternalNotes(String cargoCode, String internalNotes);

        // ===== PAYMENT OPERATIONS =====

        /**
         * Create payment URL for cargo booking (using Payment Strategy)
         * 
         * @param request Cargo payment request (cargo booking ID + payment method)
         * @return Payment response with payment URL
         * @throws CargoNotFoundException if cargo booking not found
         */
        PaymentResponseDTO createCargoPayment(CargoPaymentRequestDTO request);

        /**
         * Execute cargo payment callback (from gateway)
         * 
         * @param cargoCode Unique cargo code
         * @param paymentId Payment ID from gateway
         * @param payerId   Payer ID from gateway (for PayPal)
         * @return Payment response after execution
         * @throws CargoNotFoundException if cargo not found
         */
        PaymentResponseDTO executeCargoPayment(String cargoCode, String paymentId, String payerId);

        // ===== PDF EXPORT =====

        /**
         * Export cargo booking to PDF
         * 
         * @param cargoCode Unique cargo code
         * @return PDF as byte array
         * @throws CargoNotFoundException if cargo not found
         */
        byte[] exportCargoToPdf(String cargoCode);

        // ===== VALIDATION =====

        /**
         * Validate cargo booking with trip (similar to validateBookingTrip for tickets)
         * Used by driver/staff to verify cargo belongs to trip before pickup/delivery
         * 
         * @param tripId    Trip ID to validate against
         * @param cargoCode Unique cargo code
         * @return Cargo validation response with detailed information
         * @throws CargoNotFoundException   if cargo not found
         * @throws IllegalArgumentException if cargo does not belong to trip
         */
        CargoValidationResponseDTO validateCargoTrip(Long tripId, String cargoCode);

        // ===== QR CODE VERIFICATION =====

        /**
         * Confirm cargo pickup by QR code verification
         * Validates JWT token from QR and updates cargo status to DELIVERED
         * 
         * @param request CargoQRVerifyRequestDTO with QR token and staff ID
         * @return CargoQRVerifyResponseDTO with verification result
         * @throws CargoBookingException  if QR token is invalid or expired
         * @throws CargoNotFoundException if cargo not found
         */
        CargoQRVerifyResponseDTO confirmCargoPickupByQR(CargoQRVerifyRequestDTO request);
}
