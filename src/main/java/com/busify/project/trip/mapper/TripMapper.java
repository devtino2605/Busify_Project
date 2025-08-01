package com.busify.project.trip.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.busify.project.trip.dto.response.RouteInfoResponseDTO;
import com.busify.project.trip.dto.response.TripDetailResponse;
import com.busify.project.trip.dto.response.TripFilterResponseDTO;
import com.busify.project.trip.dto.response.TripStopResponse;
import com.busify.project.trip.entity.Trip;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TripMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static TripFilterResponseDTO toDTO(Trip trip, Double averageRating) {
        if (trip == null)
            return null;

        TripFilterResponseDTO dto = new TripFilterResponseDTO();
        dto.setTrip_id(trip.getId());
        dto.setDeparture_time(trip.getDepartureTime());
        dto.setArrival_time(trip.getEstimatedArrivalTime());
        dto.setPrice_per_seat(trip.getPricePerSeat());
        dto.setStatus(trip.getStatus());
        if (trip.getRoute() != null) {
            dto.setDuration(trip.getRoute().getDefaultDurationMinutes());

            RouteInfoResponseDTO routeDto = new RouteInfoResponseDTO();
            routeDto.setStart_location(trip.getRoute().getStartLocation().getAddress() + "; "
                    + trip.getRoute().getStartLocation().getCity());
            routeDto.setEnd_location(
                    trip.getRoute().getEndLocation().getAddress() + "; " + trip.getRoute().getEndLocation().getCity());
            dto.setRoute(routeDto);
        }

        if (trip.getBus() != null) {
            dto.setOperator_name(trip.getBus().getOperator().getName());
            dto.setAmenities(trip.getBus().getAmenities());
        }

        dto.setAverage_rating(averageRating != null ? averageRating : 0.0);

        return dto;
    }

    public static Map<String, Object> toTripDetail(TripDetailResponse detailMap, List<TripStopResponse> tripStops) {
        Map<String, Object> tripDetailJson = new HashMap<>();

        // 1. --- Thông tin cấp cao của chuyến đi ---
        tripDetailJson.put("trip_id", detailMap.getId());
        tripDetailJson.put("operator_name", detailMap.getOperatorName());
        tripDetailJson.put("departure_time", detailMap.getDepartureTime());
        tripDetailJson.put("arrival_time", detailMap.getEstimatedArrivalTime());
        tripDetailJson.put("estimated_duration", formatDuration(detailMap.getEstimatedDurationMinutes()));
        tripDetailJson.put("available_seats", detailMap.getAvailableSeats());
        tripDetailJson.put("total_seats", detailMap.getBusSeats());
        tripDetailJson.put("price_per_seat", detailMap.getPricePerSeat());
        // Xử lý giá trị null cho rating và reviews
        tripDetailJson.put("average_rating", detailMap.getAverageRating() != null ? detailMap.getAverageRating() : 0.0);
        tripDetailJson.put("total_reviews", detailMap.getTotalReviews() != null ? detailMap.getTotalReviews() : 0);

        // 2. --- Thông tin tuyến đường (Route) ---
        Map<String, Object> route = new HashMap<>();
        // THÊM: route_id vào trong đối tượng route
        route.put("route_id", detailMap.getRouteId());

        // Địa điểm bắt đầu
        Map<String, Object> startLocation = new HashMap<>();
        startLocation.put("address", detailMap.getStartAddress());
        startLocation.put("city", detailMap.getStartCity());
        startLocation.put("longitude", detailMap.getStartLongitude());
        startLocation.put("latitude", detailMap.getStartLatitude());
        route.put("start_location", startLocation);

        // Địa điểm kết thúc
        Map<String, Object> endLocation = new HashMap<>();
        endLocation.put("address", detailMap.getEndAddress());
        endLocation.put("city", detailMap.getEndCity());
        endLocation.put("longitude", detailMap.getEndLongitude());
        endLocation.put("latitude", detailMap.getEndLatitude());
        route.put("end_location", endLocation);

        tripDetailJson.put("route", route);

        // 3. --- Điểm dừng trên tuyến (Route Stops) ---
        List<Map<String, Object>> routeStops = new ArrayList<>();
        if (tripStops != null) {
            for (TripStopResponse stop : tripStops) {
                Map<String, Object> stopMap = new HashMap<>();
                stopMap.put("address", stop.getAddress());
                stopMap.put("city", stop.getCity());
                stopMap.put("longitude", stop.getLongitude());
                stopMap.put("latitude", stop.getLatitude());
                stopMap.put("time_offset_from_start", stop.getTimeOffsetFromStart());
                routeStops.add(stopMap);
            }
        }
        tripDetailJson.put("route_stop", routeStops);

        // 4. --- Thông tin xe buýt (Bus) ---
        Map<String, Object> bus = new HashMap<>();
        bus.put("name", detailMap.getBusName());
        bus.put("layout_id", detailMap.getBusLayoutId());
        bus.put("license_plate", detailMap.getBusLicensePlate());
        // SỬA: Phân tích chuỗi JSON amenities thành List<String>
        bus.put("amenities", parseAmenities(detailMap.getBusAmenities()));
        tripDetailJson.put("bus", bus);

        return tripDetailJson;
    }

    /**
     * Chuyển đổi số phút thành chuỗi định dạng "X giờ Y phút".
     */
    private static String formatDuration(Integer minutes) {
        if (minutes == null || minutes < 0) {
            return "Không xác định";
        }
        if (minutes == 0) {
            return "0 phút";
        }
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;

        StringBuilder duration = new StringBuilder();
        if (hours > 0) {
            duration.append(hours).append(" giờ");
        }
        if (remainingMinutes > 0) {
            if (hours > 0) {
                duration.append(" ");
            }
            duration.append(remainingMinutes).append(" phút");
        }
        return duration.toString();
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
}
