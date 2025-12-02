package com.busify.project.trip.service.impl;

import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.bus_operator.repository.BusOperatorRepository;
import com.busify.project.review.repository.ReviewRepository;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.audit_log.service.AuditLogService;
import com.busify.project.trip.dto.response.*;
import com.busify.project.user.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.busify.project.user.entity.User;
import com.busify.project.location.enums.LocationRegion;
import com.busify.project.trip.entity.Trip;
import com.busify.project.route.dto.response.RouteResponse;
import com.busify.project.seat_layout.entity.SeatLayout;
import com.busify.project.seat_layout.repository.SeatLayoutRepository;
import com.busify.project.trip.dto.request.TripFilterRequestDTO;
import com.busify.project.trip.dto.request.TripUpdateStatusRequest;
import com.busify.project.trip.enums.TripStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

import com.busify.project.trip.exception.TripOperationException;
import com.busify.project.trip.mapper.TripMapper;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.trip.service.TripService;
import com.busify.project.trip.service.TripScoringService;
import com.busify.project.trip.service.TripScoringService.TripWithScore;
import com.busify.project.ticket.dto.response.TicketSeatStatusReponse;
import com.busify.project.ticket.service.TicketService;
import com.busify.project.booking.service.BookingService;
import com.busify.project.cargo.service.CargoService;
import com.busify.project.bus.dto.response.BusLayoutResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private BusOperatorRepository busOperatorRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketService ticketService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private CargoService cargoService;
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private SeatLayoutRepository seatLayoutRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TripScoringService tripScoringService;

    @Override
    public List<TripFilterResponseDTO> getAllTrips() {
        return tripRepository.findAll()
                .stream()
                .map(trip -> TripMapper.toDTO(trip, getAverageRating(trip.getId()), bookingRepository))
                .collect(Collectors.toList());
    }

    public List<TripFilterResponseDTO> getTripsForCurrentDriver() {
        // Lấy thông tin user hiện tại từ JWT
        Optional<String> currentUserEmail = jwtUtils.getCurrentUserLogin();

        // System.out.println("=== DEBUG: getTripsForCurrentDriver ===");
        // System.out.println("Current user email: " +
        // currentUserEmail.orElse("NOT_FOUND"));

        if (currentUserEmail.isEmpty()) {
            throw new IllegalStateException("Người dùng chưa đăng nhập");
        }

        // Tìm user theo email
        User currentUser = userRepository.findByEmailIgnoreCase(currentUserEmail.get())
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy thông tin người dùng"));

        // System.out.println("Current user ID: " + currentUser.getId());
        // System.out.println(
        // "Current user role: " + (currentUser.getRole() != null ?
        // currentUser.getRole().getName() : "NO_ROLE"));

        // Kiểm tra xem user có phải là Employee không
        // Optional<Employee> employeeOpt =
        // employeeRepository.findById(currentUser.getId());
        // if (employeeOpt.isPresent()) {
        // Employee employee = employeeOpt.get();
        // System.out.println("Found employee with ID: " + employee.getId());
        // System.out.println("Employee driver license: " +
        // employee.getDriverLicenseNumber());
        // } else {
        // System.out.println("No employee found for user ID: " + currentUser.getId());
        // }

        // Lấy tất cả trips và log thông tin driver
        // List<Trip> allTrips = tripRepository.findAll();
        // System.out.println("Total trips found: " + allTrips.size());

        // for (Trip trip : allTrips) {
        // System.out.println("Trip ID: " + trip.getId() +
        // " | Driver: " + (trip.getDriver() != null ? trip.getDriver().getId() :
        // "NULL") +
        // " | Driver matches current user: "
        // + (trip.getDriver() != null &&
        // trip.getDriver().getId().equals(currentUser.getId())));
        // }

        // Lấy thời gian hiện tại để lọc chuyến đi
        LocalDateTime currentTime = LocalDateTime.now();

        // Lấy trips của driver hiện tại và chỉ hiển thị những chuyến đi chưa khởi hành
        List<TripFilterResponseDTO> result = tripRepository
                .findUpcomingTripsByDriverId(currentUser.getId(), currentTime)
                .stream()
                .map(trip -> TripMapper.toDTO(trip, getAverageRating(trip.getId()), bookingRepository))
                .collect(Collectors.toList());

        // System.out.println("Filtered trips for current driver: " + result.size());
        // System.out.println("=== END DEBUG ===");

        return result;
    }

    @Override
    public FilterResponseDTO filterTrips(TripFilterRequestDTO filter, int page, int size) {
        Logger logger = Logger.getLogger(TripServiceImpl.class.getName());
        logger.info(filter.toString());

        List<String> amenitiesList = filter.getAmenities() != null ? Arrays.asList(filter.getAmenities()) : null;
        List<String> busModelsList = filter.getBusModels() != null ? Arrays.asList(filter.getBusModels()) : null;

        String sortBy = filter.getSortBy() != null ? filter.getSortBy() : "departureTime";
        String sortDirection = filter.getSortDirection() != null ? filter.getSortDirection() : "ASC";

        if ("recommended".equals(sortBy)) {
            return filterTripsWithScoring(filter, page, size, amenitiesList, busModelsList);
        }

        return filterTripsWithTraditionalSort(filter, page, size, sortBy, sortDirection, amenitiesList, busModelsList);
    }

    /**
     * Filter trips using scoring algorithm for "recommended" sort
     */
    private FilterResponseDTO filterTripsWithScoring(TripFilterRequestDTO filter, int page, int size,
            List<String> amenitiesList, List<String> busModelsList) {

        List<Trip> allTrips = tripRepository.filterTripsForScoring(
                filter.getOperatorName(),
                filter.getUntilTime(),
                filter.getDepartureDate(),
                filter.getStartCity(),
                filter.getEndCity(),
                TripStatus.on_sell,
                filter.getAvailableSeats());

        List<Trip> filteredTrips = allTrips.stream()
                .filter(trip -> {
                    if (amenitiesList != null && !amenitiesList.isEmpty()) {
                        Map<String, Object> tripAmenities = trip.getBus().getAmenities();
                        if (tripAmenities == null || tripAmenities.isEmpty()) {
                            return false;
                        }
                        return amenitiesList.stream()
                                .allMatch(amenity -> Boolean.TRUE.equals(tripAmenities.get(amenity)));
                    }
                    return true;
                })
                .filter(trip -> {
                    if (busModelsList != null && !busModelsList.isEmpty()) {
                        return busModelsList.contains(trip.getBus().getModel().getName());
                    }
                    return true;
                })
                .collect(Collectors.toList());

        List<TripWithScore> scoredTrips = tripScoringService.scoreAndSort(filteredTrips, filter);

        int totalElements = scoredTrips.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalElements);

        List<TripWithScore> pageContent = scoredTrips.subList(startIndex, endIndex);

        List<TripFilterResponseDTO> tripDTOs = pageContent.stream()
                .map(tripWithScore -> TripMapper.toDTO(tripWithScore.getTrip(),
                        getAverageRating(tripWithScore.getTrip().getId()), bookingRepository))
                .collect(Collectors.toList());

        return new FilterResponseDTO(page, size, totalPages, page == 0, page >= totalPages - 1, tripDTOs);
    }

    /**
     * Filter trips using traditional sorting (price, departureTime, etc.)
     */
    private FilterResponseDTO filterTripsWithTraditionalSort(TripFilterRequestDTO filter, int page, int size,
            String sortBy, String sortDirection, List<String> amenitiesList, List<String> busModelsList) {

        if ("price".equals(sortBy)) {
            sortBy = "pricePerSeat";
        } else {
            sortBy = "departureTime";
        }

        Sort sort = "DESC".equalsIgnoreCase(sortDirection) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        if (filter.getSortBySecondary() != null) {
            String secondarySortBy = filter.getSortBySecondary();
            if ("price".equals(secondarySortBy)) {
                secondarySortBy = "pricePerSeat";
            } else {
                secondarySortBy = "departureTime";
            }
            String secondaryDirection = filter.getSortDirectionSecondary() != null ? filter.getSortDirectionSecondary()
                    : "ASC";
            Sort secondarySort = "DESC".equalsIgnoreCase(secondaryDirection) ? Sort.by(secondarySortBy).descending()
                    : Sort.by(secondarySortBy).ascending();
            sort = sort.and(secondarySort);
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Trip> tripPage = tripRepository.filterTrips(
                filter.getOperatorName(),
                filter.getUntilTime(),
                filter.getDepartureDate(),
                filter.getStartCity(),
                filter.getEndCity(),
                TripStatus.on_sell,
                filter.getAvailableSeats(),
                pageable);

        List<Trip> trips = tripPage.getContent();

        List<TripFilterResponseDTO> tripDTOs = trips.stream()
                .filter(trip -> {
                    if (amenitiesList != null && !amenitiesList.isEmpty()) {
                        Map<String, Object> tripAmenities = trip.getBus().getAmenities();
                        if (tripAmenities == null || tripAmenities.isEmpty()) {
                            return false;
                        }
                        return amenitiesList.stream()
                                .allMatch(amenity -> Boolean.TRUE.equals(tripAmenities.get(amenity)));
                    }
                    return true;
                })
                .filter(trip -> {
                    if (busModelsList != null && !busModelsList.isEmpty()) {
                        return busModelsList.contains(trip.getBus().getModel().getName());
                    }
                    return true;
                })
                .map(trip -> TripMapper.toDTO(trip, getAverageRating(trip.getId()), bookingRepository))
                .collect(Collectors.toList());

        if (tripDTOs.isEmpty()) {
            return new FilterResponseDTO(
                    page, size, tripPage.getTotalPages(),
                    tripPage.isFirst(), tripPage.isLast(), new ArrayList<>());
        }

        return new FilterResponseDTO(page, size, tripPage.getTotalPages(),
                tripPage.isFirst(), tripPage.isLast(), tripDTOs);
    }

    public List<TripFilterResponseDTO> searchTrips(LocalDateTime departureDate, LocalDateTime untilTime,
            Integer availableSeats,
            Long startLocation, Long endLocation, TripStatus status) {
        List<Trip> trips = tripRepository.searchTrips(departureDate, untilTime, startLocation, endLocation, status,
                availableSeats);
        return trips.stream()
                .map(trip -> TripMapper.toDTO(trip, getAverageRating(trip.getId()), bookingRepository))
                .collect(Collectors.toList());
    }

    private Double getAverageRating(Long tripId) {
        Double rating = reviewRepository.findAverageRatingByTripId(tripId);
        if (rating == null)
            return 0.0;

        return Math.round(rating * 10.0) / 10.0;
    }

    public List<TripResponse> findTopUpcomingTripByOperator() {
        List<TopOperatorRatingDTO> operators = busOperatorRepository.findTopRatedOperatorId(PageRequest.of(0, 5));

        List<Trip> trips = new ArrayList<>();

        // Map để lưu rating của mỗi operator
        Map<Long, Double> operatorRatings = operators.stream()
                .collect(Collectors.toMap(TopOperatorRatingDTO::getOperatorId, TopOperatorRatingDTO::getAverageRating));

        for (TopOperatorRatingDTO operator : operators) {
            Trip trip = tripRepository.findUpcomingTripsByOperator(operator.getOperatorId(), LocalDateTime.now());
            if (trip != null) {
                trips.add(trip);
            }
        }
        List<TripResponse> tripsResponses = trips.stream().limit(4).map(trip -> TripResponse
                .builder()
                .trip_id(trip.getId())
                .operator_name(trip.getBus().getOperator().getName()).route(
                        RouteResponse.builder()
                                .start_location(trip.getRoute().getStartLocation().getName())
                                .end_location(trip.getRoute().getEndLocation().getName())
                                .default_duration_minutes(trip.getRoute().getDefaultDurationMinutes())
                                .build())
                .arrival_time(trip.getEstimatedArrivalTime())
                .price_per_seat(trip.getPricePerSeat())
                .available_seats((int) (trip.getBus().getTotalSeats() - trip.getBookings().stream()
                        .filter(b -> b.getStatus() != BookingStatus.canceled_by_user
                                && b.getStatus() != BookingStatus.canceled_by_operator)
                        .count()))
                .departure_time(trip.getDepartureTime())
                .status(trip.getStatus())
                .average_rating(operatorRatings.get(trip.getBus().getOperator().getId()))
                .build()).collect(Collectors.toList());

        if (tripsResponses.isEmpty()) {
            return new ArrayList<>();
        }
        return tripsResponses;
    }

    @Override
    public Map<String, Object> getTripDetailById(Long tripId) {
        try {
            // get trip detail by ID
            TripDetailResponse tripDetail = tripRepository.findTripDetailById(tripId);
            // get trip stop by ID
            List<TripStopResponse> tripStops = tripRepository.findTripStopsById(tripId);
            // lấy danh sách hình ảnh bus
            List<BusImageResponse> busImages = tripRepository.findBusImagesByBusId(tripDetail.getBusId());
            // mapper to Map<String, Object> using mapper toTripDetail
            return TripMapper.toTripDetail(tripDetail, tripStops, busImages);
        } catch (Exception e) {
            throw TripOperationException.processingFailed(e);
        }

    }

    @Override
    public List<TripRouteResponse> getTripRouteById(Long routeId) {
        try {
            return tripRepository.findUpcomingTripsByRoute(routeId);
        } catch (Exception e) {
            throw TripOperationException.processingFailed(e);

        }
    }

    @Override
    public List<TripFilterResponseDTO> getTripRouteByIdExcludingTrip(Long tripId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new IllegalArgumentException("Trip not found"));
        Long routeId = trip.getRoute().getId();
        final List<TripFilterResponseDTO> similarTrips = tripRepository.findUpcomingTripsByRouteExcludingTrip(routeId,
                tripId).stream()
                .map(t -> TripMapper.toDTO(t, getAverageRating(t.getId()), bookingRepository))
                .collect(Collectors.toList());
        System.out.println("Similar trips found: " + similarTrips.get(0));
        return similarTrips;
    }

    @Override
    public List<TripStopResponse> getTripStopsById(Long tripId) {
        try {
            return tripRepository.findTripStopsById(tripId);
        } catch (Exception e) {
            throw TripOperationException.processingFailed(e);
        }
    }

    public List<Map<String, Object>> getNextTripsOfOperator(Long operatorId) {
        List<NextTripsOfOperatorResponseDTO> nextTrips = tripRepository.findNextTripsByOperator(operatorId);
        if (nextTrips.isEmpty()) {
            return List.of(Map.of("message", "No upcoming trips found for this operator."));
        }

        return nextTrips.stream().map(TripMapper::toNextTripsOfOperatorResponse).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> updateTripStatus(Long tripId, TripUpdateStatusRequest request) {
        try {
            Trip trip = tripRepository.findById(tripId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chuyến đi với ID: " + tripId));

            TripStatus oldStatus = trip.getStatus();

            // Kiểm tra logic chuyển đổi trạng thái
            validateStatusTransition(trip.getStatus(), request.getStatus());

            // Cập nhật trạng thái
            trip.setStatus(request.getStatus());
            tripRepository.save(trip);

            // Audit log for trip status update
            User currentUser = getCurrentUser();
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("UPDATE");
            auditLog.setTargetEntity("TRIP_STATUS");
            auditLog.setTargetId(tripId);
            auditLog.setDetails(String.format("{\"oldStatus\":\"%s\",\"newStatus\":\"%s\",\"reason\":\"%s\"}",
                    oldStatus, request.getStatus(), request.getReason()));
            auditLog.setUser(currentUser);
            auditLogService.save(auditLog);

            // Logic tự động hủy vé khi trip chuyển sang departed
            int cancelledTickets = 0;
            Map<String, Integer> cargoResult = null;
            if (request.getStatus() == TripStatus.departed) {
                System.out.println("=== DEBUG: Trip status changed to departed, calling auto-cancel tickets ===");
                cancelledTickets = ticketService.autoCancelValidTicketsWhenTripDeparted(tripId);
                System.out.println("Auto-cancelled tickets count: " + cancelledTickets);

                // Auto-process cargo: PICKED_UP → IN_TRANSIT, others → CANCELLED
                System.out.println("=== DEBUG: Processing cargo bookings for departed trip ===");
                cargoResult = cargoService.autoProcessCargoWhenTripDeparted(tripId);
                System.out.println("Cargo processing result: " + cargoResult);
            }

            // Logic tự động hoàn thành booking khi trip chuyển sang arrived
            int completedBookings = 0;
            Map<String, Integer> cargoArrivedResult = null;
            if (request.getStatus() == TripStatus.arrived) {
                System.out.println("=== DEBUG: Trip status changed to arrived, calling auto-complete bookings ===");
                completedBookings = bookingService.markBookingsAsCompletedWhenTripArrived(tripId);
                System.out.println("Auto-completed bookings count: " + completedBookings);

                // Auto-process cargo: IN_TRANSIT → ARRIVED
                System.out.println("=== DEBUG: Processing cargo bookings for arrived trip ===");
                cargoArrivedResult = cargoService.autoProcessCargoWhenTripArrived(tripId);
                System.out.println("Cargo arrival processing result: " + cargoArrivedResult);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cập nhật trạng thái chuyến đi thành công");
            response.put("tripId", tripId);
            response.put("oldStatus", oldStatus);
            response.put("newStatus", request.getStatus());
            response.put("reason", request.getReason());

            // Thêm thông tin về việc tự động hủy vé
            if (cancelledTickets > 0) {
                response.put("autoCancelledTickets", cancelledTickets);
                response.put("autoCancelMessage",
                        String.format("Đã tự động hủy %d vé chưa sử dụng do chuyến đi đã khởi hành", cancelledTickets));
            }

            // Thêm thông tin về cargo khi trip departed
            if (cargoResult != null && !cargoResult.isEmpty()) {
                response.put("cargoProcessing", cargoResult);
                int inTransit = cargoResult.getOrDefault("inTransit", 0);
                int cancelled = cargoResult.getOrDefault("cancelled", 0);
                response.put("cargoMessage",
                        String.format("Đã xử lý hàng hóa: %d đang vận chuyển, %d đã hủy", inTransit, cancelled));
            }

            // Thêm thông tin về cargo khi trip arrived
            if (cargoArrivedResult != null && !cargoArrivedResult.isEmpty()) {
                response.put("cargoArrivalProcessing", cargoArrivedResult);
                int arrived = cargoArrivedResult.getOrDefault("arrived", 0);
                response.put("cargoArrivalMessage",
                        String.format("Đã cập nhật %d hàng hóa sang trạng thái đã đến nơi", arrived));
            }

            // Thêm thông tin về việc tự động xử lý cargo
            if (cargoResult != null && (cargoResult.get("inTransit") > 0 || cargoResult.get("cancelled") > 0)) {
                response.put("autoProcessedCargo", cargoResult);
                response.put("cargoProcessMessage",
                        String.format("Đã tự động xử lý cargo: %d đang vận chuyển, %d bị hủy do chưa lấy hàng",
                                cargoResult.get("inTransit"), cargoResult.get("cancelled")));
            }

            // Thêm thông tin về việc tự động hoàn thành booking
            if (completedBookings > 0) {
                response.put("autoCompletedBookings", completedBookings);
                response.put("autoCompleteMessage",
                        String.format("Đã tự động hoàn thành %d booking do chuyến đi đã đến nơi", completedBookings));
            }

            // Thêm thông tin chi tiết chuyến đi
            TripDetailResponse tripDetail = tripRepository.findTripDetailById(tripId);
            List<TripStopResponse> tripStops = tripRepository.findTripStopsById(tripId);
            List<BusImageResponse> busImages = tripRepository.findBusImagesByBusId(tripDetail.getBusId());
            response.putAll(TripMapper.toTripDetail(tripDetail, tripStops, busImages));

            return response;
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return errorResponse;
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Lỗi hệ thống khi cập nhật trạng thái: " + e.getMessage());
            return errorResponse;
        }
    }

    private void validateStatusTransition(TripStatus currentStatus, TripStatus newStatus) {
        // Logic kiểm tra tính hợp lệ của việc chuyển đổi trạng thái

        // Không thể thay đổi trạng thái của chuyến đã hoàn thành hoặc đã hủy
        if (currentStatus == TripStatus.arrived || currentStatus == TripStatus.cancelled) {
            throw new IllegalStateException("Không thể thay đổi trạng thái của chuyến đi đã " +
                    (currentStatus == TripStatus.arrived ? "hoàn thành" : "hủy"));
        }

        // Chỉ cho phép chuyển đổi theo logic nghiệp vụ
        switch (currentStatus) {
            case scheduled:
                // Từ scheduled có thể chuyển sang departed hoặc delayed
                if (newStatus != TripStatus.departed && newStatus != TripStatus.delayed) {
                    throw new IllegalStateException(
                            "Từ trạng thái scheduled chỉ có thể chuyển sang departed hoặc delayed");
                }
                break;
            case on_sell:
                // Giữ nguyên logic cũ cho on_sell
                if (newStatus != TripStatus.departed && newStatus != TripStatus.delayed &&
                        newStatus != TripStatus.cancelled) {
                    throw new IllegalStateException(
                            "Từ trạng thái on_sell chỉ có thể chuyển sang departed, delayed hoặc cancelled");
                }
                break;
            case departed:
                // Từ departed chỉ có thể chuyển sang delayed, arrived hoặc cancelled
                if (newStatus != TripStatus.delayed && newStatus != TripStatus.arrived &&
                        newStatus != TripStatus.cancelled) {
                    throw new IllegalStateException(
                            "Từ trạng thái departed chỉ có thể chuyển sang delayed, arrived hoặc cancelled");
                }
                break;
            case delayed:
                // Từ delayed chỉ có thể chuyển sang arrived hoặc cancelled
                if (newStatus != TripStatus.arrived && newStatus != TripStatus.cancelled) {
                    throw new IllegalStateException(
                            "Từ trạng thái delayed chỉ có thể chuyển sang arrived hoặc cancelled");
                }
                break;
            case arrived:
            case cancelled:
                // Đã được xử lý ở trên - không thể chuyển sang trạng thái khác
                break;
        }
    }

    @Override
    public List<TripByDriverResponseDTO> getTripsByDriverId(Long driverId) {
        List<Object[]> results = tripRepository.findTripsByDriverId(driverId);
        List<TripByDriverResponseDTO> trips = new ArrayList<>();

        for (Object[] result : results) {
            // Convert Timestamp to LocalDateTime safely
            LocalDateTime departureTime = null;
            LocalDateTime estimatedArrivalTime = null;

            if (result[1] != null) {
                if (result[1] instanceof Timestamp) {
                    departureTime = ((Timestamp) result[1]).toLocalDateTime();
                } else if (result[1] instanceof LocalDateTime) {
                    departureTime = (LocalDateTime) result[1];
                }
            }

            if (result[2] != null) {
                if (result[2] instanceof Timestamp) {
                    estimatedArrivalTime = ((Timestamp) result[2]).toLocalDateTime();
                } else if (result[2] instanceof LocalDateTime) {
                    estimatedArrivalTime = (LocalDateTime) result[2];
                }
            }

            TripByDriverResponseDTO trip = TripByDriverResponseDTO.builder()
                    .tripId(((Number) result[0]).longValue())
                    .departureTime(departureTime)
                    .estimatedArrivalTime(estimatedArrivalTime)
                    .status((String) result[3])
                    .pricePerSeat((BigDecimal) result[4])
                    .operatorName((String) result[5])
                    .routeId(((Number) result[6]).longValue())
                    .startCity((String) result[7])
                    .startAddress((String) result[8])
                    .endCity((String) result[9])
                    .endAddress((String) result[10])
                    .busLicensePlate((String) result[11])
                    .busModel((String) result[12])
                    .availableSeats(((Number) result[13]).intValue())
                    .totalSeats(((Number) result[14]).intValue())
                    .averageRating(result[15] != null ? ((Number) result[15]).doubleValue() : 0.0)
                    .build();
            trips.add(trip);
        }

        return trips;
    }

    @Override
    public List<TripFilterResponseDTO> getUpcomingTripsForDriver(Long driverId) {
        LocalDateTime currentTime = LocalDateTime.now();

        return tripRepository.findUpcomingTripsByDriverId(driverId, currentTime)
                .stream()
                .map(trip -> TripMapper.toDTO(trip, getAverageRating(trip.getId()), bookingRepository))
                .collect(Collectors.toList());
    }

    @Override
    public List<TopTripRevenueDTO> getTop10TripsByRevenueAndYear(Integer year) {
        {
            LocalDate now = LocalDate.now();
            int reportYear = (year != null) ? year : now.getYear();
            return tripRepository.findTop10TripsByRevenueAndYear(reportYear);
        }
    }

    /**
     * Helper method to get current user for audit logging
     */
    private User getCurrentUser() {
        try {
            String currentUserEmail = jwtUtils.getCurrentUserLogin().orElse(null);
            if (currentUserEmail != null) {
                return userRepository.findByEmail(currentUserEmail).orElse(null);
            }
            return null;
        } catch (Exception e) {
            // Return null if unable to get current user (e.g., system operations)
            return null;
        }
    }

    @Override
    public TripResponseByRegionDTO getTripsEachRegion() {
        final List<TripRouteResponse> northTrips = tripRepository.findTripsByRegion(LocationRegion.NORTH);
        final List<TripRouteResponse> centralTrips = tripRepository.findTripsByRegion(LocationRegion.CENTRAL);
        final List<TripRouteResponse> southTrips = tripRepository.findTripsByRegion(LocationRegion.SOUTH);
        TripResponseByRegionDTO response = new TripResponseByRegionDTO();
        response.setTripsByRegion(Map.of(
                LocationRegion.NORTH, northTrips,
                LocationRegion.CENTRAL, centralTrips,
                LocationRegion.SOUTH, southTrips));
        return response;
    }

    @Override
    public NextTripSeatsStatusResponseDTO getNextTripSeatsStatus(Long tripId) {
        tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chuyến đi với ID: " + tripId));
        final Optional<SeatLayout> seatLayout = seatLayoutRepository.findSeatLayoutByTripId(tripId);
        if (seatLayout.isEmpty()) {
            throw new IllegalArgumentException("Chưa có sơ đồ ghế cho chuyến đi với ID: " + tripId);
        }
        NextTripSeatStatusDTO response = tripRepository.getNextTripSeatStatus(tripId);

        JsonNode layout = objectMapper.convertValue(seatLayout.get().getLayoutData(), JsonNode.class);
        int rows = layout.get("rows").asInt();
        int columns = layout.get("cols").asInt();
        int floors = layout.has("floors") ? layout.get("floors").asInt() : 1;
        final BusLayoutResponseDTO busLayout = new BusLayoutResponseDTO(rows, columns, floors);

        final List<TicketSeatStatusReponse> seatStatuses = ticketService.getTicketSeatStatusByTripId(tripId);

        NextTripSeatsStatusResponseDTO responseDTO = new NextTripSeatsStatusResponseDTO();
        responseDTO.setBusSeatsCount(response.getBusSeatsCount());
        responseDTO.setCheckedSeatsCount(response.getCheckedSeatsCount());
        responseDTO.setBookedSeatsCount(response.getBookedSeatsCount());
        responseDTO.setBusLayout(busLayout);
        responseDTO.setTripId(tripId);
        responseDTO.setSeatStatuses(seatStatuses);
        return responseDTO;
    }
}
