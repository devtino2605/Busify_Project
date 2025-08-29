package com.busify.project.bus.service.impl;

import com.busify.project.bus.dto.response.BusDetailResponseDTO;
import com.busify.project.bus.dto.response.BusForOperatorResponse;
import com.busify.project.bus.dto.response.BusLayoutResponseDTO;
import com.busify.project.bus.entity.Bus;
import com.busify.project.bus.mapper.BusMapper;
import com.busify.project.bus.repository.BusRepository;
import com.busify.project.bus.service.BusService;
import com.busify.project.bus_operator.entity.BusOperator;
import com.busify.project.bus_operator.repository.BusOperatorRepository;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.seat_layout.repository.SeatLayoutRepository;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;
    private final SeatLayoutRepository seatLayoutRepository;
    private final BusOperatorRepository busOperatorRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtil;

    @Override
    public BusLayoutResponseDTO getBusSeatLayoutMap(Long busId) {
        return busRepository.findById(busId)
                .map(bus -> {
                    if (bus.getSeatLayout() == null) {
                        throw new IllegalArgumentException("Seat layout is null for bus ID: " + busId);
                    }
                    return seatLayoutRepository.findById(bus.getSeatLayout().getId())
                            .map(seatLayout -> {
                                JsonNode layout = objectMapper.convertValue(seatLayout.getLayoutData(), JsonNode.class);
                                int rows = layout.get("rows").asInt();
                                int columns = layout.get("cols").asInt();
                                int floors = layout.has("floors") ? layout.get("floors").asInt() : 1;

                                return new BusLayoutResponseDTO(rows, columns, floors);
                            })
                            .orElseThrow(
                                    () -> new IllegalArgumentException("Seat layout not found for bus ID: " + busId));
                })
                .orElseThrow(() -> new IllegalArgumentException("Bus not found with ID: " + busId));
    }

    @Override
    public List<BusDetailResponseDTO> getBusesByOperatorId(Long operatorId) {
        final BusOperator operator = busOperatorRepository.findById(operatorId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid operator ID: " + operatorId));
        return busRepository.findByOperator(operator)
                .stream()
                .map(bus -> BusDetailResponseDTO.builder()
                        .id(bus.getId())
                        .licensePlate(bus.getLicensePlate())
                        .totalSeats(bus.getTotalSeats())
                        .status(bus.getStatus())
                        .amenities(bus.getAmenities())
                        .modelName(bus.getModel().getName())
                        .seatLayoutId(bus.getSeatLayout() != null ? bus.getSeatLayout().getId() : (int) 0L)
                        .totalSeats(bus.getTotalSeats())
                        .operatorId(operatorId)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<BusForOperatorResponse> getAllBuses() {
        // 1. Lấy email user hiện tại từ JWT context
        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Lấy operatorId từ user
        Long operatorId = busOperatorRepository.findOperatorIdByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy BusOperator cho user này"));

        return busRepository.findBusesByOperator(operatorId);
    }
}
