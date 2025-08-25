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
import com.busify.project.employee.entity.Employee;
import com.busify.project.seat_layout.entity.SeatLayout;
import com.busify.project.seat_layout.repository.SeatLayoutRepository;
import com.busify.project.bus.service.BusMGMTService;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        bus.setLicensePlate(requestDTO.getLicensePlate());

        // 1. Lấy email user hiện tại từ JWT context
        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Nếu user là employee thì lấy operator_id
        Long operatorId = null;
        if (user instanceof Employee) {
            operatorId = ((Employee) user).getOperator().getId();
        }

        // Lấy BusOperator từ DB
        assert operatorId != null;
        BusOperator operator = busOperatorRepository.findById(operatorId)
                .orElseThrow(() -> new RuntimeException("Bus Operator không tồn tại"));
        bus.setOperator(operator);

        // Lấy BusModel
        BusModel model = busModelRepository.findById(requestDTO.getModelId())
                .orElseThrow(() -> new RuntimeException("Bus Model không tồn tại"));
        bus.setModel(model);

        // Lấy SeatLayout từ DB
        SeatLayout seatLayout = seatLayoutRepository.findById(requestDTO.getSeatLayoutId())
                .orElseThrow(() -> new RuntimeException("Seat Layout không tồn tại"));
        bus.setSeatLayout(seatLayout);

        // Tính totalSeats từ layout_data (cols * rows * floors)
        try {
            JSONObject layoutData = new JSONObject(seatLayout.getLayoutData());
            int cols = (int) layoutData.get("cols");
            int rows = (int) layoutData.get("rows");
            int floors = (int) layoutData.get("floors");
            bus.setTotalSeats(cols * rows * floors);
        } catch (Exception e) {
            throw new RuntimeException("Dữ liệu seat layout không hợp lệ", e);
        }

        bus.setAmenities(requestDTO.getAmenities());
        bus.setStatus(requestDTO.getStatus());

        Bus savedBus = busRepository.save(bus);

        return BusMGMTMapper.toBusDetailResponseDTO(savedBus);
    }

    @Override
    public BusDetailResponseDTO updateBus(Long id, BusMGMTRequestDTO requestDTO) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bus không tồn tại"));

        // 1. Lấy email user hiện tại từ JWT context
        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Nếu user là employee thì lấy operator_id
        Long operatorId = null;
        if (user instanceof Employee) {
            operatorId = ((Employee) user).getOperator().getId();
        }

        if (requestDTO.getLicensePlate() != null) {
            bus.setLicensePlate(requestDTO.getLicensePlate());
        }

        if (requestDTO.getModelId() != null) {
            BusModel model = busModelRepository.findById(requestDTO.getModelId())
                    .orElseThrow(() -> new RuntimeException("Bus Model không tồn tại"));
            bus.setModel(model);
        }

        if (requestDTO.getOperatorId() != null) {
            BusOperator operator = busOperatorRepository.findById(operatorId)
                    .orElseThrow(() -> new RuntimeException("Bus Operator không tồn tại"));
            bus.setOperator(operator);
        }

        if (requestDTO.getSeatLayoutId() != null) {
            SeatLayout seatLayout = seatLayoutRepository.findById(requestDTO.getSeatLayoutId())
                    .orElseThrow(() -> new RuntimeException("Seat Layout không tồn tại"));
            bus.setSeatLayout(seatLayout);

            try {
                JSONObject layoutData = new JSONObject(seatLayout.getLayoutData());
                int cols = (int) layoutData.get("cols");
                int rows = (int) layoutData.get("rows");
                int floors = (int) layoutData.get("floors");
                bus.setTotalSeats(cols * rows * floors);
            } catch (Exception e) {
                throw new RuntimeException("Dữ liệu seat layout không hợp lệ", e);
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
                .orElseThrow(() -> new RuntimeException("Bus không tồn tại"));

        // Kiểm tra xem bus có đang được sử dụng trong bảng trips không
        boolean existsInTrips = tripRepository.existsByBusId(bus.getId());
        if (existsInTrips) {
            throw new RuntimeException("Không thể xóa xe vì đang tồn tại trong chuyến đi");
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
                bus.getSeatLayout().getName()
        );
    }


    @Override
    public ApiResponse<?> getAllBuses(String keyword, BusStatus status, List<String> amenities, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        // 1. Lấy email user hiện tại từ JWT context
        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Nếu user là employee thì lấy operator_id
        Long operatorId = null;
        if (user instanceof Employee) {
            operatorId = ((Employee) user).getOperator().getId();
        }

        // 3. Chuyển List<String> amenities sang JSON
        String amenitiesJson;
        try {
            amenitiesJson = (amenities == null || amenities.isEmpty())
                    ? "[]"
                    : new ObjectMapper().writeValueAsString(amenities);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Lỗi khi chuyển amenities sang JSON", e);
        }

        // 4. Gọi repository với filter theo operatorId
        Page<Bus> busPage = busRepository.searchAndFilterBuses(
                keyword,
                status != null ? status.name() : null,
                amenities,
                amenitiesJson,
                operatorId,
                pageable
        );

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
