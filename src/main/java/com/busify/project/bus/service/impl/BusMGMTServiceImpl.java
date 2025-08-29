package com.busify.project.bus.service.impl;

import com.busify.project.bus.dto.request.BusMGMTRequestDTO;
import com.busify.project.bus.dto.response.BusDeleteResponseDTO;
import com.busify.project.bus.dto.response.BusDetailResponseDTO;
import com.busify.project.bus.entity.Bus;
import com.busify.project.bus.enums.BusStatus;
import com.busify.project.bus.mapper.BusMGMTMapper;
import com.busify.project.bus.repository.BusRepository;
import com.busify.project.bus_model.entity.BusModel;
import com.busify.project.bus_model.repository.BusModelRepository;
import com.busify.project.bus_operator.entity.BusOperator;
import com.busify.project.bus_operator.repository.BusOperatorRepository;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.seat_layout.entity.SeatLayout;
import com.busify.project.seat_layout.repository.SeatLayoutRepository;
import com.busify.project.bus.service.BusMGMTService;
import com.busify.project.bus.exception.BusCreationException;
import com.busify.project.bus.exception.BusUpdateException;
import com.busify.project.bus.exception.BusNotFoundException;
import com.busify.project.bus.exception.BusDeleteException;
import com.busify.project.bus.exception.BusSeatLayoutException;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusMGMTServiceImpl implements BusMGMTService {

    private final BusRepository busRepository;
    private final BusOperatorRepository busOperatorRepository;
    private final SeatLayoutRepository seatLayoutRepository;
    private final BusModelRepository busModelRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtil;

    @Override
    public BusDetailResponseDTO addBus(BusMGMTRequestDTO requestDTO) {
        Bus bus = new Bus();
        if (busRepository.existsByLicensePlate(requestDTO.getLicensePlate())) {
            throw new ValidationException("Biển số xe đã tồn tại trong hệ thống");
        } else {
            bus.setLicensePlate(requestDTO.getLicensePlate());
        }

        // 1. Lấy email user hiện tại từ JWT context
        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Lấy operatorId từ user
        Long operatorId = busOperatorRepository.findOperatorIdByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy BusOperator cho user này"));

        // Lấy BusOperator từ DB
        assert operatorId != null;
        BusOperator operator = busOperatorRepository.findById(operatorId)
                .orElseThrow(() -> BusCreationException.invalidModel(operatorId));
        bus.setOperator(operator);

        // Lấy BusModel
        BusModel model = busModelRepository.findById(requestDTO.getModelId())
                .orElseThrow(() -> BusCreationException.invalidModel(requestDTO.getModelId()));
        bus.setModel(model);

        // Lấy SeatLayout từ DB
        SeatLayout seatLayout = seatLayoutRepository.findById(requestDTO.getSeatLayoutId())
                .orElseThrow(() -> BusCreationException.invalidSeatLayout(requestDTO.getSeatLayoutId().longValue()));
        bus.setSeatLayout(seatLayout);

        // Tính totalSeats từ layout_data (cols * rows * floors)
        try {
            JSONObject layoutData = new JSONObject(seatLayout.getLayoutData());
            int cols = (int) layoutData.get("cols");
            int rows = (int) layoutData.get("rows");
            int floors = (int) layoutData.get("floors");
            bus.setTotalSeats(cols * rows * floors);
        } catch (Exception e) {
            throw BusCreationException.invalidSeatLayoutData("Unable to parse seat layout data: " + e.getMessage());
        }

        bus.setAmenities(requestDTO.getAmenities());
        bus.setStatus(requestDTO.getStatus());

        Bus savedBus = busRepository.save(bus);

        return BusMGMTMapper.toBusDetailResponseDTO(savedBus);
    }

    @Override
    public BusDetailResponseDTO updateBus(Long id, BusMGMTRequestDTO requestDTO) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> BusUpdateException.busNotFound(id));

        // 1. Lấy email user hiện tại từ JWT context
        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Lấy operatorId từ user
        Long operatorId = busOperatorRepository.findOperatorIdByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy BusOperator cho user này"));

        if (busRepository.existsByLicensePlateAndIdNot(requestDTO.getLicensePlate(), id)) {
            throw new ValidationException("Biển số xe đã tồn tại trong hệ thống");
        } else {
            bus.setLicensePlate(requestDTO.getLicensePlate());
        }

        if (requestDTO.getModelId() != null) {
            BusModel model = busModelRepository.findById(requestDTO.getModelId())
                    .orElseThrow(() -> BusUpdateException.invalidModel(requestDTO.getModelId()));
            bus.setModel(model);
        }

        if (requestDTO.getOperatorId() != null) {
            BusOperator operator = busOperatorRepository.findById(operatorId)
                    .orElseThrow(() -> BusUpdateException.unauthorizedOperator(id, operatorId));
            bus.setOperator(operator);
        }

        if (requestDTO.getSeatLayoutId() != null) {
            SeatLayout seatLayout = seatLayoutRepository.findById(requestDTO.getSeatLayoutId())
                    .orElseThrow(() -> BusUpdateException.invalidSeatLayout(requestDTO.getSeatLayoutId().longValue()));
            bus.setSeatLayout(seatLayout);

            try {
                JSONObject layoutData = new JSONObject(seatLayout.getLayoutData());
                int cols = (int) layoutData.get("cols");
                int rows = (int) layoutData.get("rows");
                int floors = (int) layoutData.get("floors");
                bus.setTotalSeats(cols * rows * floors);
            } catch (Exception e) {
                throw BusSeatLayoutException.invalidLayoutData("Unable to parse seat layout data: " + e.getMessage());
            }
        }

        if (requestDTO.getAmenities() != null) {
            bus.setAmenities(requestDTO.getAmenities());
        }

        if (requestDTO.getStatus() != null) {
            bus.setStatus(requestDTO.getStatus());
        }

        Bus updatedBus = busRepository.save(bus);

        return BusMGMTMapper.toBusDetailResponseDTO(updatedBus);
    }

    @Override
    public BusDeleteResponseDTO deleteBus(Long id, boolean isDelete) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new BusNotFoundException(id));

        // Kiểm tra xem bus có đang được sử dụng trong bảng trips không
        boolean existsInTrips = tripRepository.existsByBusId(bus.getId());
        if (existsInTrips) {
            throw BusDeleteException.hasActiveTrips(id, 1);
        }

        if (isDelete) {
            busRepository.delete(bus);
        }

        // Dù isDelete là true hay false thì vẫn trả thông tin bus
        return new BusDeleteResponseDTO(
                bus.getId(),
                bus.getLicensePlate(),
                bus.getModel().getName(),
                bus.getOperator().getName(),
                bus.getSeatLayout().getName());
    }

    @Override
    public ApiResponse<?> getAllBuses(String keyword, BusStatus status, List<String> amenities, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        // 1. Lấy email user hiện tại từ JWT context
        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Lấy operatorId từ user
        Long operatorId = busOperatorRepository.findOperatorIdByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy BusOperator cho user này"));

        // 3. Chuyển List<String> amenities sang JSON
        String amenitiesJson;
        try {
            amenitiesJson = (amenities == null || amenities.isEmpty())
                    ? "[]"
                    : new ObjectMapper().writeValueAsString(amenities);
        } catch (JsonProcessingException e) {
            throw new BusCreationException("Lỗi khi chuyển amenities sang JSON", e);
        }

        // 4. Gọi repository với filter theo operatorId
        Page<Bus> busPage = busRepository.searchAndFilterBuses(
                keyword,
                status != null ? status.name() : null,
                amenities,
                amenitiesJson,
                operatorId,
                pageable);

        // 5. Map sang DTO
        List<BusDetailResponseDTO> content = busPage.stream()
                .map(BusMGMTMapper::toBusDetailResponseDTO)
                .collect(Collectors.toList());

        // 6. Đóng gói dữ liệu trả về
        Map<String, Object> response = new HashMap<>();
        response.put("result", content);
        response.put("pageNumber", busPage.getNumber() + 1);
        response.put("pageSize", busPage.getSize());
        response.put("totalRecords", busPage.getTotalElements());
        response.put("totalPages", busPage.getTotalPages());
        response.put("hasNext", busPage.hasNext());
        response.put("hasPrevious", busPage.hasPrevious());

        return ApiResponse.success("Lấy danh sách xe khách thành công", response);
    }
}
