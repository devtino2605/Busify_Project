package com.busify.project.cargo.mapper;

import com.busify.project.cargo.dto.response.*;
import com.busify.project.cargo.entity.CargoBooking;
import com.busify.project.cargo.entity.CargoImage;
import com.busify.project.cargo.entity.CargoTracking;
import com.busify.project.location.entity.Location;
import com.busify.project.payment.entity.Payment;
import com.busify.project.trip.entity.Trip;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CargoMapper
 * 
 * Mapper for converting Cargo entities to DTOs
 * Follows the same pattern as TripMapper, TicketMapper, etc.
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-07
 */
@Component
public class CargoMapper {

        /**
         * Convert CargoBooking entity to CargoBookingResponseDTO
         * Used for simple cargo booking responses
         */
        public CargoBookingResponseDTO toCargoBookingResponse(CargoBooking cargoBooking) {
                if (cargoBooking == null) {
                        return null;
                }

                Trip trip = cargoBooking.getTrip();
                Location pickup = cargoBooking.getPickupLocation();
                Location dropoff = cargoBooking.getDropoffLocation();

                // Generate trip route name
                String tripRoute = null;
                if (trip != null && trip.getRoute() != null) {
                        tripRoute = trip.getRoute().getName();
                }

                // Calculate estimated times based on trip departure
                LocalDateTime estimatedPickup = null;
                LocalDateTime estimatedDelivery = null;
                if (trip != null && trip.getDepartureTime() != null) {
                        estimatedPickup = trip.getDepartureTime();
                        estimatedDelivery = trip.getEstimatedArrivalTime();
                }

                StringBuffer dropoffLocation = new StringBuffer();
                StringBuffer pickupLocation = new StringBuffer();

                return CargoBookingResponseDTO.builder()
                                .cargoBookingId(cargoBooking.getCargoBookingId())
                                .cargoCode(cargoBooking.getCargoCode())
                                .tripId(trip != null ? trip.getId() : null)
                                .tripRoute(tripRoute)

                                // Sender info
                                .senderName(cargoBooking.getSenderName())
                                .senderPhone(cargoBooking.getSenderPhone())

                                // Receiver info
                                .receiverName(cargoBooking.getReceiverName())
                                .receiverPhone(cargoBooking.getReceiverPhone())

                                // Cargo info
                                .cargoType(cargoBooking.getCargoType())
                                .cargoTypeDisplay(cargoBooking.getCargoType().getDisplayName())
                                .weight(cargoBooking.getWeight())
                                .dimensions(cargoBooking.getDimensions())

                                // Locations
                                .pickupLocationId(pickup != null ? pickup.getId() : null)
                                .pickupLocationName(pickup != null ? (pickupLocation.append(pickup.getName())
                                                .append("-").append(pickup.getCity())).toString() : null)
                                .dropoffLocationId(dropoff != null ? dropoff.getId() : null)
                                .dropoffLocationName(dropoff != null ? (dropoffLocation.append(dropoff.getName())
                                                .append("-").append(dropoff.getCity())).toString() : null)

                                // Fees
                                .cargoFee(cargoBooking.getCargoFee())
                                .insuranceFee(cargoBooking.getInsuranceFee())
                                .totalAmount(cargoBooking.getTotalAmount())

                                // Status
                                .status(cargoBooking.getStatus())
                                .statusDisplay(cargoBooking.getStatus().getDisplayName())

                                // Timestamps
                                .createdAt(cargoBooking.getCreatedAt())
                                .estimatedPickupTime(estimatedPickup)
                                .estimatedDeliveryTime(estimatedDelivery)
                                .build();
        }

        /**
         * Convert CargoBooking entity to CargoListResponseDTO
         * Used for cargo listings with summary information
         */
        public CargoListResponseDTO toCargoListResponse(CargoBooking cargoBooking) {
                if (cargoBooking == null) {
                        return null;
                }

                Trip trip = cargoBooking.getTrip();
                Location pickup = cargoBooking.getPickupLocation();
                Location dropoff = cargoBooking.getDropoffLocation();

                StringBuffer routeName = new StringBuffer();

                // Build bus info
                CargoListResponseDTO.BusInfo busInfo = null;
                if (trip.getBus() != null) {
                        busInfo = CargoListResponseDTO.BusInfo.builder()
                                        .model(trip.getBus().getModel() != null
                                                        ? trip.getBus().getModel().getName()
                                                        : null)
                                        .licensePlate(trip.getBus().getLicensePlate())
                                        .build();
                }

                // Build payment info
                CargoListResponseDTO.PaymentInfo paymentInfo = null;
                if (cargoBooking.getPayment() != null) {
                        paymentInfo = CargoListResponseDTO.PaymentInfo.builder()
                                        .amount(cargoBooking.getPayment().getAmount())
                                        .method(cargoBooking.getPayment().getPaymentMethod())
                                        .timestamp(cargoBooking.getPayment().getPaidAt())
                                        .build();
                }

                // Extract image URLs
                List<String> imageUrls = cargoBooking.getImages() != null
                                ? cargoBooking.getImages().stream()
                                                .map(img -> img.getImageUrl())
                                                .collect(Collectors.toList())
                                : new ArrayList<>();

                return CargoListResponseDTO.builder()
                                .cargoBookingId(cargoBooking.getCargoBookingId())
                                .cargoCode(cargoBooking.getCargoCode())
                                .status(cargoBooking.getStatus())
                                .statusDisplay(cargoBooking.getStatus().getDisplayName())
                                .tripId(trip.getId())
                                .routeName(trip.getRoute() != null
                                                ? routeName.append(trip.getRoute().getStartLocation().getName())
                                                                .append("-")
                                                                .append(trip.getRoute().getStartLocation().getCity())
                                                                .append(" -> ")
                                                                .append(trip.getRoute().getEndLocation().getName())
                                                                .append("-")
                                                                .append(trip.getRoute().getEndLocation().getCity())
                                                                .toString()
                                                : null)
                                .departureTime(trip.getDepartureTime())
                                .pickupLocationName(pickup != null ? pickup.getName() : null)
                                .dropoffLocationName(dropoff != null ? dropoff.getName() : null)
                                .senderName(cargoBooking.getSenderName())
                                .senderPhone(cargoBooking.getSenderPhone())
                                .receiverName(cargoBooking.getReceiverName())
                                .receiverPhone(cargoBooking.getReceiverPhone())
                                .cargoType(cargoBooking.getCargoType())
                                .cargoTypeDisplay(cargoBooking.getCargoType().getDisplayName())
                                .weight(cargoBooking.getWeight())
                                .totalAmount(cargoBooking.getTotalAmount())
                                .bus(busInfo)
                                .payment(paymentInfo)
                                .images(imageUrls)
                                .createdAt(cargoBooking.getCreatedAt())
                                .deliveredAt(cargoBooking.getDeliveredAt())
                                .build();
        }

        /**
         * Convert CargoBooking entity to CargoDetailResponseDTO
         * Used for detailed cargo information with all related data
         */
        public CargoDetailResponseDTO toCargoDetailResponse(CargoBooking cargoBooking) {
                if (cargoBooking == null) {
                        return null;
                }

                Trip trip = cargoBooking.getTrip();

                // Build trip info
                CargoDetailResponseDTO.TripInfo tripInfo = CargoDetailResponseDTO.TripInfo.builder()
                                .tripId(trip.getId())
                                .routeName(trip.getRoute() != null ? trip.getRoute().getName() : null)
                                .departureTime(trip.getDepartureTime())
                                .arrivalTime(trip.getEstimatedArrivalTime())
                                .busOperatorName(
                                                trip.getBus() != null && trip.getBus().getOperator() != null
                                                                ? trip.getBus().getOperator().getName()
                                                                : null)
                                .driverName(trip.getDriver() != null ? trip.getDriver().getFullName() : null)
                                .driverPhone(trip.getDriver() != null ? trip.getDriver().getPhoneNumber() : null)
                                .build();

                // Build sender contact info
                CargoDetailResponseDTO.ContactInfo sender = CargoDetailResponseDTO.ContactInfo.builder()
                                .name(cargoBooking.getSenderName())
                                .phone(cargoBooking.getSenderPhone())
                                .email(cargoBooking.getSenderEmail())
                                .address(cargoBooking.getSenderAddress())
                                .build();

                // Build receiver contact info
                CargoDetailResponseDTO.ContactInfo receiver = CargoDetailResponseDTO.ContactInfo.builder()
                                .name(cargoBooking.getReceiverName())
                                .phone(cargoBooking.getReceiverPhone())
                                .email(cargoBooking.getReceiverEmail())
                                .address(cargoBooking.getReceiverAddress())
                                .build();

                // Build cargo details
                CargoDetailResponseDTO.CargoInfo cargoInfo = CargoDetailResponseDTO.CargoInfo.builder()
                                .type(cargoBooking.getCargoType())
                                .typeDisplay(cargoBooking.getCargoType().getDisplayName())
                                .description(cargoBooking.getDescription())
                                .weight(cargoBooking.getWeight())
                                .dimensions(cargoBooking.getDimensions())
                                .declaredValue(cargoBooking.getDeclaredValue())
                                .build();

                // Build location info
                Location pickupLoc = cargoBooking.getPickupLocation();
                CargoDetailResponseDTO.LocationInfo pickup = CargoDetailResponseDTO.LocationInfo.builder()
                                .locationId(pickupLoc != null ? pickupLoc.getId() : null)
                                .locationName(pickupLoc != null ? pickupLoc.getName() : null)
                                .locationCity(pickupLoc != null ? pickupLoc.getCity() : null)
                                .build();

                Location dropoffLoc = cargoBooking.getDropoffLocation();
                CargoDetailResponseDTO.LocationInfo dropoff = CargoDetailResponseDTO.LocationInfo.builder()
                                .locationId(dropoffLoc != null ? dropoffLoc.getId() : null)
                                .locationName(dropoffLoc != null ? dropoffLoc.getName() : null)
                                .locationCity(dropoffLoc != null ? dropoffLoc.getCity() : null)
                                .build();

                // Build payment info if exists
                Payment payment = cargoBooking.getPayment();
                CargoDetailResponseDTO.PaymentInfo paymentInfo = null;
                if (payment != null) {
                        paymentInfo = CargoDetailResponseDTO.PaymentInfo.builder()
                                        .paymentId(payment.getPaymentId())
                                        .paymentMethod(payment.getPaymentMethod())
                                        .paymentMethodDisplay(payment.getPaymentMethod().name())
                                        .cargoFee(cargoBooking.getCargoFee())
                                        .insuranceFee(cargoBooking.getInsuranceFee())
                                        .totalAmount(cargoBooking.getTotalAmount())
                                        .status(payment.getStatus())
                                        .statusDisplay(payment.getStatus().name())
                                        .transactionId(payment.getTransactionCode())
                                        .paidAt(payment.getPaidAt())
                                        .build();
                }

                // Build tracking history
                List<CargoTrackingResponseDTO> trackingHistory = cargoBooking.getTrackingHistory()
                                .stream()
                                .map(this::toCargoTrackingResponse)
                                .collect(Collectors.toList());

                // Build images list
                List<CargoDetailResponseDTO.ImageInfo> images = cargoBooking.getImages()
                                .stream()
                                .map(this::toImageInfo)
                                .collect(Collectors.toList());

                // Build main detail response
                return CargoDetailResponseDTO.builder()
                                .cargoBookingId(cargoBooking.getCargoBookingId())
                                .cargoCode(cargoBooking.getCargoCode())
                                .trip(tripInfo)
                                .sender(sender)
                                .receiver(receiver)
                                .cargo(cargoInfo)
                                .pickup(pickup)
                                .dropoff(dropoff)
                                .payment(paymentInfo)
                                .status(cargoBooking.getStatus())
                                .statusDisplay(cargoBooking.getStatus().getDisplayName())
                                .trackingHistory(trackingHistory)
                                .images(images)
                                .createdAt(cargoBooking.getCreatedAt())
                                .updatedAt(cargoBooking.getUpdatedAt())
                                .confirmedAt(cargoBooking.getConfirmedAt())
                                .pickedUpAt(cargoBooking.getPickedUpAt())
                                .deliveredAt(cargoBooking.getDeliveredAt())
                                .specialInstructions(cargoBooking.getSpecialInstructions())
                                .build();
        }

        /**
         * Convert CargoImage entity to ImageInfo DTO (nested in CargoDetailResponseDTO)
         */
        public CargoDetailResponseDTO.ImageInfo toImageInfo(CargoImage image) {
                if (image == null) {
                        return null;
                }

                return CargoDetailResponseDTO.ImageInfo.builder()
                                .imageId(image.getImageId())
                                .imageUrl(image.getImageUrl())
                                .imageType(image.getImageType().name())
                                .description(image.getDescription())
                                .uploadedAt(image.getUploadedAt())
                                .build();
        }

        /**
         * Convert CargoTracking entity to CargoTrackingResponseDTO
         * Used for tracking API responses
         */
        public CargoTrackingResponseDTO toCargoTrackingResponse(CargoTracking tracking) {
                if (tracking == null) {
                        return null;
                }

                return CargoTrackingResponseDTO.builder()
                                .trackingId(tracking.getTrackingId())
                                .status(tracking.getStatus())
                                .statusDisplay(tracking.getStatus().getDisplayName())
                                .location(tracking.getLocation())
                                .notes(tracking.getNotes())
                                .createdAt(tracking.getTrackedAt())
                                .updatedById(tracking.getUpdatedBy() != null ? tracking.getUpdatedBy().getId() : null)
                                .updatedByName(tracking.getUpdatedBy() != null ? tracking.getUpdatedBy().getEmail()
                                                : "System")
                                .updatedByRole(tracking.getUpdatedBy() != null
                                                && tracking.getUpdatedBy().getRole() != null
                                                                ? tracking.getUpdatedBy().getRole().getName()
                                                                : null)
                                .build();
        }

}
