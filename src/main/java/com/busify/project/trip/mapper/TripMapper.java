package com.busify.project.trip.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.trip.dto.response.*;
import com.busify.project.trip.entity.Trip;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

public class TripMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static TripFilterResponseDTO toDTO(Trip trip, Double averageRating, BookingRepository bookingRepository) {
        if (trip == null)
            return null;

        TripFilterResponseDTO dto = new TripFilterResponseDTO();
        dto.setTrip_id(trip.getId());
        dto.setDeparture_time(trip.getDepartureTime());
        dto.setArrival_time(trip.getEstimatedArrivalTime());
        dto.setPrice_per_seat(trip.getPricePerSeat());
        dto.setStatus(trip.getStatus());

        if (trip.getRoute() != null) {
            // dto.setDuration(trip.getRoute().getDefaultDurationMinutes());

            RouteInfoResponseDTO routeDto = new RouteInfoResponseDTO();
            routeDto.setStart_location(trip.getRoute().getStartLocation().getAddress() + "; "
                    + trip.getRoute().getStartLocation().getCity());
            routeDto.setEnd_location(
                    trip.getRoute().getEndLocation().getAddress() + "; " + trip.getRoute().getEndLocation().getCity());
            dto.setRoute(routeDto);
        }

        if (trip.getBus() != null) {
            dto.setOperator_name(trip.getBus().getOperator().getName());
            dto.setOperator_avatar(trip.getBus().getOperator().getAvatar());
            dto.setAmenities(trip.getBus().getAmenities());
        }

        dto.setAverage_rating(averageRating != null ? averageRating : 0.0);

        List<Bookings> bookings = bookingRepository.findByTripId(trip.getId());

        // Các trạng thái bị hủy thì không tính
        List<BookingStatus> excludedStatuses = Arrays.asList(
                BookingStatus.canceled_by_user,
                BookingStatus.canceled_by_operator);

        // Tính số ghế đã đặt (count thực tế từ seat_number, không chỉ số booking)
        int bookedSeats = bookings.stream()
                .filter(b -> !excludedStatuses.contains(b.getStatus())) // loại bỏ booking bị hủy
                .mapToInt(b -> {
                    if (b.getSeatNumber() == null || b.getSeatNumber().isEmpty())
                        return 0;
                    return b.getSeatNumber().split(",").length; // tách theo dấu phẩy
                })
                .sum();

        int totalSeats = trip.getBus().getTotalSeats();
        dto.setAvailable_seats(totalSeats - bookedSeats);
        dto.setTotal_seats(totalSeats);

        return dto;
    }

    public static Map<String, Object> toTripDetail(TripDetailResponse detailMap, List<TripStopResponse> tripStops,
            List<BusImageResponse> busImages) {
        Map<String, Object> tripDetailJson = new HashMap<>();

        // trip
        tripDetailJson.put("trip_id", detailMap.getId());
        tripDetailJson.put("departure_time", detailMap.getDepartureTime());
        tripDetailJson.put("arrival_time", detailMap.getEstimatedArrivalTime());
        tripDetailJson.put("available_seats", detailMap.getAvailableSeats());
        tripDetailJson.put("price_per_seat", detailMap.getPricePerSeat());
        tripDetailJson.put("original_price", detailMap.getOriginalPrice());
        tripDetailJson.put("discount_amount", detailMap.getDiscountAmount());
        tripDetailJson.put("has_discount", detailMap.getDiscountAmount() > 0);
        tripDetailJson.put("average_rating", detailMap.getAverageRating());
        tripDetailJson.put("total_reviews", detailMap.getTotalReviews());
        // Xử lý giá trị null cho rating và reviews

        // 2. --- Thông tin tuyến đường (Route) ---
        Map<String, Object> route = new HashMap<>();

        Map<String, Object> startLocation = new HashMap<>();
        startLocation.put("id", detailMap.getStartLocationId());
        startLocation.put("address", detailMap.getStartAddress());
        startLocation.put("city", detailMap.getStartCity());
        startLocation.put("longitude", detailMap.getStartLongitude());
        startLocation.put("latitude", detailMap.getStartLatitude());

        // Địa điểm kết thúc
        Map<String, Object> endLocation = new HashMap<>();
        endLocation.put("id", detailMap.getEndLocationId());
        endLocation.put("address", detailMap.getEndAddress());
        endLocation.put("city", detailMap.getEndCity());
        endLocation.put("longitude", detailMap.getEndLongitude());
        endLocation.put("latitude", detailMap.getEndLatitude());

        route.put("route_id", detailMap.getRouteId());
        route.put("start_location", startLocation);
        route.put("end_location", endLocation);
        route.put("estimated_duration", detailMap.getEstimatedDurationMinutes()); // Return raw minutes from database
                                                                                  // without formatting

        tripDetailJson.put("route", route);

        // 3. --- Điểm dừng trên tuyến (Route Stops) ---
        List<Map<String, Object>> routeStops = new ArrayList<>();
        if (tripStops != null) {
            for (TripStopResponse stop : tripStops) {
                Map<String, Object> stopMap = new HashMap<>();
                stopMap.put("id", stop.getLocationId());
                stopMap.put("name", stop.getName());
                stopMap.put("address", stop.getAddress());
                stopMap.put("city", stop.getCity());
                stopMap.put("longitude", stop.getLongitude());
                stopMap.put("latitude", stop.getLatitude());
                stopMap.put("time_offset_from_start", stop.getTimeOffsetFromStart());
                routeStops.add(stopMap);
            }
        }
        tripDetailJson.put("route_stops", routeStops);

        // 4. --- Thông tin xe buýt (Bus) ---
        Map<String, Object> bus = new HashMap<>();
        bus.put("bus_id", detailMap.getBusId());
        bus.put("name", detailMap.getBusName());
        bus.put("license_plate", detailMap.getBusLicensePlate());
        bus.put("total_seats", detailMap.getBusSeats());
        bus.put("amenities", parseAmenities(detailMap.getBusAmenities()));

        // Thêm images
        List<Map<String, Object>> imageList = new ArrayList<>();
        if (busImages != null) {
            for (BusImageResponse img : busImages) {
                Map<String, Object> imgMap = new HashMap<>();
                imgMap.put("id", img.getId());
                imgMap.put("url", img.getImageUrl());
                imgMap.put("is_primary", img.getIsPrimary());
                imageList.add(imgMap);
            }
        }
        bus.put("images", imageList);

        tripDetailJson.put("bus", bus);

        tripDetailJson.put("operator_id", detailMap.getOperatorId());
        tripDetailJson.put("operator_name", detailMap.getOperatorName());

        // 5. --- Thông tin tài xế (Driver) ---
        if (detailMap.getDriverId() != null) {
            Map<String, Object> driver = new HashMap<>();
            driver.put("driver_id", detailMap.getDriverId());
            driver.put("driver_name", detailMap.getDriverName());
            tripDetailJson.put("driver", driver);
        }

        return tripDetailJson;
    }

    /**
     * Phân tích chuỗi JSON từ cột 'amenities' thành một danh sách các tiện ích có
     * sẵn.
     *
     * @param amenitiesJson Chuỗi JSON, ví dụ: '{"wifi": true, "tv": false,
     *                      "air_conditioner": true}'
     * @return Danh sách các tiện ích có giá trị true, ví dụ: ["wifi",
     *         "air_conditioner"]
     */
    private static List<String> parseAmenities(String amenitiesJson) {
        // Trả về danh sách rỗng nếu đầu vào là null hoặc rỗng
        if (amenitiesJson == null || amenitiesJson.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            // Định nghĩa kiểu dữ liệu mà chúng ta muốn chuyển đổi
            TypeReference<Map<String, Boolean>> typeRef = new TypeReference<>() {
            };
            Map<String, Boolean> amenitiesMap = objectMapper.readValue(amenitiesJson, typeRef);

            // Lọc và chỉ lấy những key có giá trị là 'true'
            return amenitiesMap.entrySet().stream()
                    .filter(Map.Entry::getValue)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            // Ghi lại lỗi nếu có vấn đề khi phân tích JSON
            System.err.println("Không thể phân tích chuỗi JSON amenities: " + amenitiesJson);
            e.printStackTrace();
            // Trả về danh sách rỗng để tránh lỗi
            return new ArrayList<>();
        }
    }

    public static Map<String, Object> toNextTripsOfOperatorResponse(NextTripsOfOperatorResponseDTO nextTrip) {
        Map<String, Object> response = new HashMap<>();
        response.put("trip_id", nextTrip.getTripId());
        response.put("departure_time", nextTrip.getDepartureTime());
        response.put("arrival_time", nextTrip.getArrivalTime());
        response.put("duration_minutes", nextTrip.getEstimatedDurationMinutes());
        response.put("available_seats", nextTrip.getAvailableSeats());
        response.put("status", nextTrip.getBusStatus());
        response.put("start_city", nextTrip.getStartCity());
        response.put("end_city", nextTrip.getEndCity());
        response.put("start_address", nextTrip.getStartAddress());
        response.put("end_address", nextTrip.getEndAddress());

        response.put("start_longitude", nextTrip.getStartLongitude());
        response.put("start_latitude", nextTrip.getStartLatitude());

        response.put("end_longitude", nextTrip.getEndLongitude());
        response.put("end_latitude", nextTrip.getEndLatitude());

        response.put("route_id", nextTrip.getRouteId());
        response.put("route_name", nextTrip.getRouteName());

        response.put("bus_id", nextTrip.getBusId());
        response.put("license_plate", nextTrip.getBusLicensePlate());
        response.put("total_seats", nextTrip.getBusSeats());
        response.put("bus_status", nextTrip.getBusStatus());
        return response;
    }
}