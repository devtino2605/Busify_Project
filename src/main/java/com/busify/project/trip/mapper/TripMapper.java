package com.busify.project.trip.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.busify.project.trip.dto.response.RouteInfoResponseDTO;
import com.busify.project.trip.dto.response.TripFilterResponseDTO;
import com.busify.project.trip.entity.Trip;

public class TripMapper {

    public static TripFilterResponseDTO toDTO(Trip trip, Double averageRating) {
        if (trip == null) return null;

        TripFilterResponseDTO dto = new TripFilterResponseDTO();
        dto.setTrip_id(trip.getId());
        dto.setDeparture_time(trip.getDepartureTime());
        dto.setArrival_time(trip.getEstimatedArrivalTime());
        dto.setPrice_per_seat(trip.getPricePerSeat());
        dto.setStatus(trip.getStatus());
        if (trip.getRoute() != null) {
            dto.setDuration(trip.getRoute().getDefaultDurationMinutes());

            RouteInfoResponseDTO routeDto = new RouteInfoResponseDTO();
            routeDto.setStart_location(trip.getRoute().getStartLocation().getAddress()+"; "+trip.getRoute().getStartLocation().getCity());
            routeDto.setEnd_location(trip.getRoute().getEndLocation().getAddress()+"; "+trip.getRoute().getEndLocation().getCity());
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

        // 1. Thêm các thuộc tính cấp cao nhất
        tripDetailJson.put("id", detailMap.getId());
        // Giữ nguyên đối tượng Instant hoặc giá trị gốc
        tripDetailJson.put("departureTime", detailMap.getDepartureTime());
        tripDetailJson.put("price_per_seat", detailMap.getPricePerSeat());

        // 2. Xây dựng đối tượng "route"
        Map<String, Object> route = new HashMap<>();

        // 2.1. Đối tượng "startLocation"
        Map<String, Object> startLocation = new HashMap<>();
        startLocation.put("city", detailMap.getStartCity());
        startLocation.put("name", detailMap.getStartName());
        startLocation.put("address", detailMap.getStartAddress());
        startLocation.put("longtitude", detailMap.getStartLongitude());
        startLocation.put("latitude", detailMap.getStartLatitude());

        // 2.2. Đối tượng "endLocation"
        Map<String, Object> endLocation = new HashMap<>();
        endLocation.put("city", detailMap.getEndCity());
        endLocation.put("name", detailMap.getEndName());
        endLocation.put("address", detailMap.getEndAddress());
        endLocation.put("longtitude", detailMap.getEndLongitude());
        endLocation.put("latitude", detailMap.getEndLatitude());

        route.put("startLocation", startLocation);
        route.put("endLocation", endLocation);
        // Giữ nguyên số phút, không định dạng
        route.put("estimatedDuration", detailMap.getEstimatedDurationMinutes());
        // route.put("distance", ...);

        tripDetailJson.put("route", route);

        // 3. Xây dựng danh sách "route_stop"
        List<Map<String, Object>> routeStopList = new ArrayList<>();
        if (tripStops != null) {
            for (TripStopResponse stop : tripStops) {
                Map<String, Object> stopMap = new HashMap<>();
                stopMap.put("city", stop.getCity());
                stopMap.put("address", stop.getAddress());
                stopMap.put("longtitude", stop.getLongitude());
                stopMap.put("latitude", stop.getLatitude());
                stopMap.put("time_offset_from_start", stop.getTimeOffsetFromStart());
                routeStopList.add(stopMap);
            }
        }
        tripDetailJson.put("route_stop", routeStopList);

        // 4. Xây dựng đối tượng "bus"
        Map<String, Object> bus = new HashMap<>();
        bus.put("name", detailMap.getBusName());
        bus.put("seats", detailMap.getBusSeats());
        bus.put("licensePlate", detailMap.getBusLicensePlate());
        tripDetailJson.put("bus", bus);

        // 5. Xây dựng đối tượng "operator"
        Map<String, Object> operator = new HashMap<>();
        operator.put("id", detailMap.getOperatorId());
        operator.put("name", detailMap.getOperatorName());
        tripDetailJson.put("operator", operator);

        return tripDetailJson;
    }
}