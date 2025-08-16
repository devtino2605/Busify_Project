package com.busify.project.employee.service.impl;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.employee.dto.request.EmployeeMGMTAddRequestDTO;
import com.busify.project.employee.dto.request.EmployeeMGMTRequestDTO;
import com.busify.project.employee.dto.response.EmployeeDeleteResponseDTO;
import com.busify.project.employee.dto.response.EmployeeMGMTResponseDTO;
import com.busify.project.employee.entity.Employee;
import com.busify.project.employee.mapper.EmployeeMGMTMapper;
import com.busify.project.employee.repository.EmployeeRepository;
import com.busify.project.employee.service.EmployeeMGMTService;
import com.busify.project.role.entity.Role;
import com.busify.project.role.repository.RoleRepository;
import com.busify.project.trip.dto.response.TripResponse;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;
import com.busify.project.user.enums.UserStatus;
import com.busify.project.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeMGMTServiceImpl implements EmployeeMGMTService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse<?> getAllEmployees(String keyword, UserStatus status, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);

        Page<Employee> employeePage = employeeRepository.searchEmployees(keyword, status, pageable);

        List<EmployeeMGMTResponseDTO> content = employeePage.stream()
                .map(EmployeeMGMTMapper::toEmployeeDetailResponseDTO)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("result", content);
        response.put("pageNumber", employeePage.getNumber() + 1);
        response.put("pageSize", employeePage.getSize());
        response.put("totalRecords", employeePage.getTotalElements());
        response.put("totalPages", employeePage.getTotalPages());
        response.put("hasNext", employeePage.hasNext());
        response.put("hasPrevious", employeePage.hasPrevious());

        return ApiResponse.success("Lấy danh sách nhân viên thành công", response);
    }

    @Override
    public ApiResponse<?> updateEmployee(Long id, EmployeeMGMTRequestDTO requestDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với id: " + id));

        // Cập nhật Employee
        if (requestDTO.getDriverLicenseNumber() != null) {
            employee.setDriverLicenseNumber(requestDTO.getDriverLicenseNumber());
        }
        if (requestDTO.getOperatorId() != null) {
            employee.setOperator(requestDTO.getOperatorId());
        }

        employee.setAddress(requestDTO.getAddress());
        employee.setPhoneNumber(requestDTO.getPhoneNumber());
        employee.setStatus(requestDTO.getStatus());
        employee.setFullName(requestDTO.getFullName());

        employeeRepository.save(employee);

        return ApiResponse.success("Cập nhật nhân viên thành công",
                EmployeeMGMTMapper.toEmployeeDetailResponseDTO(employee));
    }

    @Override
    public EmployeeDeleteResponseDTO deleteEmployee(Long id, boolean isDelete) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        if (isDelete) {
            // Kiểm tra có trip nào đang gán driver này không
            boolean existsTrip = tripRepository.existsByDriverId(id);
            if (existsTrip) {
                throw new RuntimeException("Tài xế này đang được bố trí cho chuyến đi");
            } else {
                userRepository.delete((User) (Profile) employee);
            }
        }

        return new EmployeeDeleteResponseDTO(
                employee.getId(),
                employee.getFullName(),
                employee.getEmail()
        );
    }

    @Override
    @Transactional
    public EmployeeMGMTResponseDTO createEmployee(EmployeeMGMTAddRequestDTO dto) {
        // 1. Kiểm tra email đã tồn tại
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email đã được sử dụng");
        }

        // 2. Lấy role EMPLOYEE, nếu chưa có thì tạo mới
        Role employeeRole = roleRepository.findByName("EMPLOYEE")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("EMPLOYEE");
                    return roleRepository.save(newRole);
                });

        // 3. Tạo Employee (kế thừa User + Profile)
        Employee employee = new Employee();
        employee.setEmail(dto.getEmail());
        employee.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        employee.setRole(employeeRole);

        employee.setFullName(dto.getFullName());

        employee = employeeRepository.save(employee);

        // 4. Map sang ResponseDTO
        return EmployeeMGMTMapper.toEmployeeDetailResponseDTO(employee);
    }
}
