package com.busify.project.trip.service.impl;

import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.bus_operator.repository.BusOperatorRepository;
import com.busify.project.review.repository.ReviewRepository;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.user.repository.UserRepository;
import com.busify.project.user.entity.User;
import com.busify.project.employee.repository.EmployeeRepository;
import com.busify.project.employee.entity.Employee;
import com.busify.project.trip.entity.Trip;
import com.busify.project.route.dto.response.RouteResponse;
import com.busify.project.trip.dto.response.TripFilterResponseDTO;
import com.busify.project.trip.dto.request.TripFilterRequestDTO;
import com.busify.project.trip.dto.request.TripUpdateStatusRequest;
import com.busify.project.trip.enums.TripStatus;
import com.busify.project.trip.dto.response.TripByDriverResponseDTO;
import com.busify.project.trip.dto.response.NextTripsOfOperatorResponseDTO;
import com.busify.project.trip.dto.response.TopOperatorRatingDTO;
import com.busify.project.trip.dto.response.TopTripRevenueDTO;
import com.busify.project.trip.dto.response.TripDetailResponse;
import com.busify.project.trip.dto.response.TripResponse;
import com.busify.project.trip.dto.response.TripRouteResponse;
import com.busify.project.trip.dto.response.TripStopResponse;
import com.busify.project.trip.entity.Trip;
import com.busify.project.trip.mapper.TripMapper;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.trip.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private EmployeeRepository employeeRepository;

    @Override
    public List<TripFilterResponseDTO> getAllTrips() {
        return tripRepository.findAll()
                .stream()
                .map(trip -> TripMapper.toDTO(trip, getAverageRating(trip.getId()), bookingRepository))
                .collect(Collectors.toList());
    }

    @Override
    public List<TripFilterResponseDTO> getTripsForCurrentDriver() {
        // Lấy thông tin user hiện tại từ JWT
        Optional<String> currentUserEmail = jwtUtils.getCurrentUserLogin();
        
        System.out.println("=== DEBUG: getTripsForCurrentDriver ===");
        System.out.println("Current user email: " + currentUserEmail.orElse("NOT_FOUND"));
        
        if (currentUserEmail.isEmpty()) {
            throw new IllegalStateException("Người dùng chưa đăng nhập");
        }
        
        // Tìm user theo email
        User currentUser = userRepository.findByEmailIgnoreCase(currentUserEmail.get())
            .orElseThrow(() -> new IllegalStateException("Không tìm thấy thông tin người dùng"));
        
        System.out.println("Current user ID: " + currentUser.getId());
        System.out.println("Current user role: " + (currentUser.getRole() != null ? currentUser.getRole().getName() : "NO_ROLE"));
        
        // Kiểm tra xem user có phải là Employee không
        Optional<Employee> employeeOpt = employeeRepository.findById(currentUser.getId());
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            System.out.println("Found employee with ID: " + employee.getId());
            System.out.println("Employee driver license: " + employee.getDriverLicenseNumber());
        } else {
            System.out.println("No employee found for user ID: " + currentUser.getId());
        }
        
        // Lấy tất cả trips và log thông tin driver
        List<Trip> allTrips = tripRepository.findAll();
        System.out.println("Total trips found: " + allTrips.size());
        
        for (Trip trip : allTrips) {
            System.out.println("Trip ID: " + trip.getId() + 
                " | Driver: " + (trip.getDriver() != null ? trip.getDriver().getId() : "NULL") +
                " | Driver matches current user: " + (trip.getDriver() != null && trip.getDriver().getId().equals(currentUser.getId())));
        }
        
        // Lấy trips của driver hiện tại
        List<TripFilterResponseDTO> result = tripRepository.findAll()
                .stream()
                .filter(trip -> trip.getDriver() != null && trip.getDriver().getId().equals(currentUser.getId()))
                .map(trip -> TripMapper.toDTO(trip, getAverageRating(trip.getId()), bookingRepository))
                .collect(Collectors.toList());
        
        System.out.println("Filtered trips for current driver: " + result.size());
        System.out.println("=== END DEBUG ===");
        
        return result;
    }

    @Override
    public List<TripFilterResponseDTO> filterTrips(TripFilterRequestDTO filter) {
        List<Trip> trips = tripRepository.findAll().stream()
                .filter(trip -> filter.getRouteId() == null || trip.getRoute().getId().equals(filter.getRouteId()))
                .filter(trip -> filter.getBusOperatorIds() == null
                        || List.of(filter.getBusOperatorIds()).contains(trip.getBus().getOperator().getId()))
                .filter(trip -> filter.getDepartureDate() == null
                        || trip.getDepartureTime().atZone(ZoneId.of("Asia/Ho_Chi_Minh"))
                                .toLocalDate().isEqual(LocalDate.parse(filter.getDepartureDate())))
                .filter(trip -> filter.getUntilTime() == null
                        || trip.getDepartureTime().atZone(ZoneId.of("Asia/Ho_Chi_Minh"))
                                .toLocalTime().isBefore(LocalTime.parse(filter.getUntilTime())))
                .filter(trip -> filter.getAvailableSeats() == null
                        || trip.getBus().getTotalSeats() - trip.getBookings().stream()
                                .filter(b -> b.getStatus() != BookingStatus.canceled_by_user
                                        && b.getStatus() != BookingStatus.canceled_by_operator)
                                .count() >= filter.getAvailableSeats())
                .filter(trip -> filter.getBusModels() == null
                        || List.of(filter.getBusModels()).contains(trip.getBus().getModel()))
                .filter(trip -> filter.getAmenities() == null
                        || Arrays.stream(filter.getAmenities())
                                .allMatch(a -> trip.getBus().getAmenities().containsKey(a)
                                        || trip.getBus().getAmenities().containsValue(a)))
                .filter(trip -> applyFilters(trip, filter))
                .collect(Collectors.toList());
        if (trips.isEmpty()) {
            return new ArrayList<>();
        }
        return trips.stream()
                .map(trip -> TripMapper.toDTO(trip, getAverageRating(trip.getId()), bookingRepository))
                .collect(Collectors.toList());
    }

    private boolean applyFilters(Trip trip, TripFilterRequestDTO filter) {
        return true;
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
            Trip trip = tripRepository.findUpcomingTripsByOperator(operator.getOperatorId(), Instant.now());
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
            // mapper to Map<String, Object> using mapper toTripDetail
            return TripMapper.toTripDetail(tripDetail, tripStops);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching trip detail for ID: " + tripId, e);
        }

    }

    @Override
    public List<TripRouteResponse> getTripRouteById(Long routeId) {
        try {
            return tripRepository.findUpcomingTripsByRoute(routeId);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching trip route for ID: " + routeId, e);

        }
    }

    @Override
    public List<TripStopResponse> getTripStopsById(Long tripId) {
        try {
            return tripRepository.findTripStopsById(tripId);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching trip stops for ID: " + tripId, e);
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

            // Kiểm tra logic chuyển đổi trạng thái
            validateStatusTransition(trip.getStatus(), request.getStatus());

            // Cập nhật trạng thái
            trip.setStatus(request.getStatus());
            tripRepository.save(trip);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cập nhật trạng thái chuyến đi thành công");
            response.put("tripId", tripId);
            response.put("oldStatus", trip.getStatus());
            response.put("newStatus", request.getStatus());
            response.put("reason", request.getReason());

            // Thêm thông tin chi tiết chuyến đi
            TripDetailResponse tripDetail = tripRepository.findTripDetailById(tripId);
            List<TripStopResponse> tripStops = tripRepository.findTripStopsById(tripId);
            response.putAll(TripMapper.toTripDetail(tripDetail, tripStops));

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
        if (currentStatus == TripStatus.arrived || currentStatus == TripStatus.cancelled) {
            throw new IllegalStateException("Không thể thay đổi trạng thái của chuyến đi đã " +
                (currentStatus == TripStatus.arrived ? "hoàn thành" : "hủy"));
        }

        if (currentStatus == TripStatus.on_time && newStatus == TripStatus.scheduled) {
            throw new IllegalStateException("Không thể chuyển từ trạng thái on_time về scheduled");
        }

        if (currentStatus == TripStatus.departed && newStatus == TripStatus.scheduled) {
            throw new IllegalStateException("Không thể chuyển từ trạng thái departed về scheduled");
        }

        if (currentStatus == TripStatus.departed && newStatus == TripStatus.on_time) {
            throw new IllegalStateException("Không thể chuyển từ trạng thái departed về on_time");
        }

        // Chỉ cho phép chuyển đổi theo logic nghiệp vụ
        switch (currentStatus) {
            case scheduled:
                if (newStatus != TripStatus.on_time && newStatus != TripStatus.delayed &&
                    newStatus != TripStatus.cancelled) {
                    throw new IllegalStateException("Từ trạng thái scheduled chỉ có thể chuyển sang on_time, delayed hoặc cancelled");
                }
                break;
            case on_time:
                if (newStatus != TripStatus.departed && newStatus != TripStatus.delayed &&
                    newStatus != TripStatus.cancelled) {
                    throw new IllegalStateException("Từ trạng thái on_time chỉ có thể chuyển sang departed, delayed hoặc cancelled");
                }
                break;
            case delayed:
                if (newStatus != TripStatus.departed && newStatus != TripStatus.cancelled) {
                    throw new IllegalStateException("Từ trạng thái delayed chỉ có thể chuyển sang departed hoặc cancelled");
                }
                break;
            case departed:
                if (newStatus != TripStatus.arrived) {
                    throw new IllegalStateException("Từ trạng thái departed chỉ có thể chuyển sang arrived");
                }
                break;
            case arrived:
            case cancelled:
                // These cases are already handled above but included for completeness
                break;
        }
    }

    @Override
    public List<TripByDriverResponseDTO> getTripsByDriverId(Long driverId) {
        List<Object[]> results = tripRepository.findTripsByDriverId(driverId);
        List<TripByDriverResponseDTO> trips = new ArrayList<>();

        for (Object[] result : results) {
            // Convert Timestamp to Instant safely
            Instant departureTime = null;
            Instant estimatedArrivalTime = null;

            if (result[1] != null) {
                if (result[1] instanceof Timestamp) {
                    departureTime = ((Timestamp) result[1]).toInstant();
                } else if (result[1] instanceof Instant) {
                    departureTime = (Instant) result[1];
                }
            }

            if (result[2] != null) {
                if (result[2] instanceof Timestamp) {
                    estimatedArrivalTime = ((Timestamp) result[2]).toInstant();
                } else if (result[2] instanceof Instant) {
                    estimatedArrivalTime = (Instant) result[2];
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
    public List<TopTripRevenueDTO> getTop10TripsByRevenueAndYear(Integer year) {
        {
            LocalDate now = LocalDate.now();
            int reportYear = (year != null) ? year : now.getYear();
            return tripRepository.findTop10TripsByRevenueAndYear(reportYear);
        }
    }
}