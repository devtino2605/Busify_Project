package com.busify.project.trip.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.busify.project.trip.dto.TripDTO;
import com.busify.project.trip.dto.response.TripDetailResponse;
import com.busify.project.trip.dto.response.TripStopResponse;
import com.busify.project.trip.entity.Trip;

public class TripMapper {

    public static TripDTO toDTO(Trip trip) {
        if (trip == null)
            return null;

        TripDTO dto = new TripDTO();
        dto.setId(trip.getId());
        dto.setDepartureTime(trip.getDepartureTime());
        dto.setEstimatedArrivalTime(trip.getEstimatedArrivalTime());
        dto.setPricePerSeat(trip.getPricePerSeat());
        dto.setStatus(trip.getStatus());

        if (trip.getRoute() != null) {
            dto.setRouteId(trip.getRoute().getId());
            dto.setRouteName(trip.getRoute().getName());
            dto.setDuration(trip.getRoute().getDefaultDurationMinutes());
        }

        if (trip.getBus() != null) {
            dto.setBusId(trip.getBus().getId());
            dto.setBusPlateNumber(trip.getBus().getLicensePlate());
            dto.setOperatorId(trip.getBus().getOperator().getId());
            dto.setSeatLayoutId(trip.getBus().getSeatLayout().getId());
            dto.setSeatLayoutName(trip.getBus().getSeatLayout().getName());
            dto.setAmenities(trip.getBus().getAmenities());
        }

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