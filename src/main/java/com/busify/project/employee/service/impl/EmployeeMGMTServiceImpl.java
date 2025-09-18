package com.busify.project.employee.service.impl;

import com.busify.project.bus_operator.entity.BusOperator;
import com.busify.project.bus_operator.repository.BusOperatorRepository;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.employee.dto.request.EmployeeMGMTAddRequestDTO;
import com.busify.project.employee.dto.request.EmployeeMGMTRequestDTO;
import com.busify.project.employee.dto.response.EmployeeDeleteResponseDTO;
import com.busify.project.employee.dto.response.EmployeeMGMTResponseDTO;
import com.busify.project.employee.entity.Employee;
import com.busify.project.employee.enums.EmployeeType;
import com.busify.project.employee.exception.EmployeeBusOperatorException;
import com.busify.project.employee.exception.EmployeeDeleteException;
import com.busify.project.employee.exception.EmployeeNotFoundException;
import com.busify.project.employee.exception.EmployeeUpdateException;
import com.busify.project.employee.mapper.EmployeeMGMTMapper;
import com.busify.project.employee.repository.EmployeeRepository;
import com.busify.project.employee.service.EmployeeMGMTService;
import com.busify.project.role.entity.Role;
import com.busify.project.role.repository.RoleRepository;
import com.busify.project.trip.enums.TripStatus;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;
import com.busify.project.user.enums.UserStatus;
import com.busify.project.user.repository.UserRepository;
import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.audit_log.service.AuditLogService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
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
    private final BusOperatorRepository busOperatorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtil;
    private final AuditLogService auditLogService;

    @Override
    public ApiResponse<?> getAllEmployees(String keyword, UserStatus status, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);

        // 1. Lấy email user hiện tại từ JWT context
        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Lấy operator_id theo user
        Long operatorId = busOperatorRepository.findOperatorIdByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy BusOperator cho user này"));

        Page<Employee> employeePage = employeeRepository.searchEmployees(keyword, status, operatorId, pageable);

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
                .orElseThrow(() -> EmployeeNotFoundException.withId(id));

        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Long operatorId = busOperatorRepository.findOperatorIdByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy BusOperator cho user này"));

        // Check không được đổi DRIVER → STAFF khi đang có trip
        if (employee.getEmployeeType() == EmployeeType.DRIVER
                && requestDTO.getEmployeeType() == EmployeeType.STAFF
                && tripRepository.existsByDriverId(employee.getId())) {
            throw new IllegalArgumentException(
                    "Không thể chuyển tài xế đang tham gia chuyến đi thành nhân viên bán vé"
            );
        }

        // Check trùng số GPLX (ngoại trừ chính employee hiện tại)
        if (requestDTO.getEmployeeType() == EmployeeType.DRIVER
                && requestDTO.getDriverLicenseNumber() != null
                && employeeRepository.existsByDriverLicenseNumberAndIdNot(
                requestDTO.getDriverLicenseNumber(), id)) {
            throw EmployeeUpdateException.duplicateDriverLicense(requestDTO.getDriverLicenseNumber());
        }

        // Nếu DRIVER đang gắn với trip có status DELAYED, DEPARTED, ON_SELL, SCHEDULED
        if (employee.getEmployeeType() == EmployeeType.DRIVER
                && requestDTO.getStatus() != null
                && requestDTO.getStatus() != UserStatus.active
                && tripRepository.existsByDriverIdAndStatusIn(
                employee.getId(),
                Arrays.asList(TripStatus.delayed, TripStatus.departed, TripStatus.on_sell, TripStatus.scheduled)
        )) {
            throw new IllegalArgumentException(
                    "Không thể đổi trạng thái khác 'Hoạt động' cho tài xế đang tham gia chuyến đi"
            );
        }

        // Cập nhật Employee
        employee.setDriverLicenseNumber(requestDTO.getDriverLicenseNumber());
        employee.setAddress(requestDTO.getAddress());
        employee.setPhoneNumber(requestDTO.getPhoneNumber());
        employee.setStatus(requestDTO.getStatus());
        employee.setFullName(requestDTO.getFullName());

        if (requestDTO.getEmployeeType() != EmployeeType.DRIVER
                && requestDTO.getEmployeeType() != EmployeeType.STAFF) {
            throw new IllegalArgumentException("Chỉ được phép cập nhật nhân viên với loại DRIVER hoặc STAFF");
        }

        Role employeeRole = roleRepository.findByName(requestDTO.getEmployeeType().name())
                .orElseThrow(() -> new IllegalArgumentException("Role không tồn tại trong hệ thống"));

        employee.setRole(employeeRole);
        employee.setEmployeeType(requestDTO.getEmployeeType());

        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
            employee.setPasswordHash(passwordEncoder.encode(requestDTO.getPassword()));
        }

        BusOperator model = busOperatorRepository.findById(operatorId)
                .orElseThrow(EmployeeBusOperatorException::operatorNotExists);
        employee.setOperator(model);

        employee = employeeRepository.save(employee);

        // Audit log
        try {
            User currentUser = getCurrentUser();
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("UPDATE");
            auditLog.setTargetEntity("EMPLOYEE");
            auditLog.setTargetId(employee.getId());
            auditLog.setDetails(String.format(
                    "{\"employee_id\":%d,\"full_name\":\"%s\",\"email\":\"%s\",\"status\":\"%s\",\"operator_id\":%d,\"action\":\"update\"}",
                    employee.getId(), employee.getFullName(), employee.getEmail(), employee.getStatus(), operatorId));
            auditLog.setUser(currentUser);
            auditLogService.save(auditLog);
        } catch (Exception e) {
            System.err.println("Failed to create audit log for employee update: " + e.getMessage());
        }

        return ApiResponse.success("Cập nhật nhân viên thành công",
                EmployeeMGMTMapper.toEmployeeDetailResponseDTO(employee));
    }

    @Override
    public EmployeeDeleteResponseDTO deleteEmployee(Long id, boolean isDelete) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> EmployeeNotFoundException.withId(id));

        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (isDelete) {
            // Kiểm tra có trip nào đang gán driver này không
            boolean existsTrip = tripRepository.existsByDriverId(id);
            boolean isDeletingSelf = employee.getId().equals(user.getId());
            if (existsTrip) {
                throw EmployeeDeleteException.hasActiveTrips(id);
            } else if (isDeletingSelf) {
                throw EmployeeDeleteException.cannotDeleteSelf(id);
            } else {
                userRepository.delete((User) (Profile) employee);

                // Audit log for employee deletion
                try {
                    User currentUser = getCurrentUser();
                    AuditLog auditLog = new AuditLog();
                    auditLog.setAction("DELETE");
                    auditLog.setTargetEntity("EMPLOYEE");
                    auditLog.setTargetId(employee.getId());
                    auditLog.setDetails(String.format("{\"employee_id\":%d,\"full_name\":\"%s\",\"email\":\"%s\",\"action\":\"hard_delete\"}", 
                            employee.getId(), employee.getFullName(), employee.getEmail()));
                    auditLog.setUser(currentUser);
                    auditLogService.save(auditLog);
                } catch (Exception e) {
                    System.err.println("Failed to create audit log for employee deletion: " + e.getMessage());
                }
            }
        }

        return new EmployeeDeleteResponseDTO(
                employee.getId(),
                employee.getFullName(),
                employee.getEmail());
    }

    @Override
    @Transactional
    public EmployeeMGMTResponseDTO createEmployee(EmployeeMGMTAddRequestDTO dto) {
        // Kiểm tra email đã tồn tại
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email đã được sử dụng");
        }

        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Long operatorId = busOperatorRepository.findOperatorIdByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy BusOperator cho user này"));

        // Chỉ cho phép DRIVER hoặc STAFF
        if (dto.getEmployeeType() != EmployeeType.DRIVER && dto.getEmployeeType() != EmployeeType.STAFF) {
            throw new IllegalArgumentException("Chỉ được phép tạo nhân viên với loại DRIVER hoặc STAFF");
        }

        Role employeeRole = roleRepository.findByName(dto.getEmployeeType().name())
                .orElseThrow(() -> new IllegalArgumentException("Role không tồn tại trong hệ thống"));

        // 3. Tạo Employee (kế thừa User + Profile)
        Employee employee = new Employee();
        employee.setEmail(dto.getEmail());
        employee.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        employee.setRole(employeeRole);

        employee.setFullName(dto.getFullName());
        employee.setEmailVerified(true);
        employee.setEmployeeType(dto.getEmployeeType());

        assert operatorId != null;
        BusOperator operator = busOperatorRepository.findById(operatorId)
                .orElseThrow(EmployeeBusOperatorException::operatorNotExists);
        employee.setOperator(operator);

        employee = employeeRepository.save(employee);

        // Audit log for employee creation
        try {
            User currentUser = getCurrentUser();
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("CREATE");
            auditLog.setTargetEntity("EMPLOYEE");
            auditLog.setTargetId(employee.getId());
            auditLog.setDetails(String.format("{\"employee_id\":%d,\"full_name\":\"%s\",\"email\":\"%s\",\"role\":\"DRIVER\",\"operator_id\":%d,\"action\":\"create\"}",
                    employee.getId(), employee.getFullName(), employee.getEmail(), operatorId));
            auditLog.setUser(currentUser);
            auditLogService.save(auditLog);
        } catch (Exception e) {
            System.err.println("Failed to create audit log for employee creation: " + e.getMessage());
        }

        // 4. Map sang ResponseDTO
        return EmployeeMGMTMapper.toEmployeeDetailResponseDTO(employee);
    }

    // Helper method to get current user from SecurityContext
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("No authenticated user found");
        }
        
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}
