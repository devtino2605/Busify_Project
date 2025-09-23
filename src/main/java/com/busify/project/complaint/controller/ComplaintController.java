package com.busify.project.complaint.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.complaint.dto.ComplaintAddCurrentUserDTO;
import com.busify.project.complaint.dto.ComplaintAddDTO;
import com.busify.project.complaint.dto.ComplaintUpdateDTO;
import com.busify.project.complaint.dto.response.ComplaintStatsDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseDetailDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseListDTO;
import com.busify.project.complaint.service.ComplaintServiceImpl;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.busify.project.complaint.dto.response.ComplaintPageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/complaints")
@Tag(name = "Complaints", description = "Complaint Management API")
public class ComplaintController {

    private final ComplaintServiceImpl complaintService;

    @Operation(summary = "Get all complaints with pagination")
    @GetMapping
    public ApiResponse<ComplaintPageResponseDTO> getAllComplaints(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            return ApiResponse.success("Lấy danh sách khiếu nại thành công",
                    complaintService.getAllComplaints(pageable));
        } catch (Exception e) {
            return ApiResponse.error(500, "Đã xảy ra lỗi khi lấy danh sách khiếu nại: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<ComplaintResponseDetailDTO> getComplaintById(@PathVariable Long id) {
        return ApiResponse.success(complaintService.getComplaintById(id));
    }

    @GetMapping("/bookings/{bookingId}")
    public ApiResponse<ComplaintResponseListDTO> getAllComplaints(@PathVariable Long bookingId) {
        return ApiResponse.success(complaintService.getAllComplaintsByBooking(bookingId));
    }

    @GetMapping("/customer/{customerId}")
    public ApiResponse<ComplaintResponseListDTO> getAllComplaintsByCustomer(@PathVariable Long customerId) {
        return ApiResponse.success(complaintService.getAllComplaintsByCustomer(customerId));
    }

    @GetMapping("/customer")
    public ApiResponse<ComplaintResponseListDTO> getAllComplaintsByCurrentCustomer() {
        return ApiResponse.success(complaintService.getAllComplaintsByCurrentCustomer());
    }

    @GetMapping("/trip/{tripId}")
    public ApiResponse<ComplaintResponseListDTO> getAllComplaintsByTrip(@PathVariable Long tripId) {
        return ApiResponse.success(complaintService.getAllComplaintsByTrip(tripId));
    }

    @PostMapping("/booking/{bookingId}")
    public ApiResponse<ComplaintResponseDTO> addComplaint(@PathVariable Long bookingId,
            @RequestBody @Valid ComplaintAddDTO complaintAddDTO) {
        return ApiResponse.success(complaintService.addComplaint(complaintAddDTO));
    }

    @PostMapping("/booking")
    public ApiResponse<ComplaintResponseDTO> addComplaintByCurrentUser(
            @RequestBody @Valid ComplaintAddCurrentUserDTO complaintAddCurrentUserDTO) {
        return ApiResponse.success(complaintService.addComplaintByCurrentUser(complaintAddCurrentUserDTO));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
        return ApiResponse.success("Complaint deleted successfully", null);
    }

    @PatchMapping("/{id}")
    public ApiResponse<ComplaintResponseDetailDTO> updateComplaint(@PathVariable Long id,
            @RequestBody @Valid ComplaintUpdateDTO complaintUpdateDTO) {
        return ApiResponse.success(complaintService.updateComplaint(id, complaintUpdateDTO));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<ComplaintResponseDetailDTO> updateComplaintStatus(@PathVariable Long id,
            @RequestBody ComplaintUpdateDTO status) {
        return ApiResponse.success(complaintService.updateComplaintStatus(id, status.getStatus()));
    }

    @GetMapping("/bus-operator/{busOperatorId}")
    public ApiResponse<ComplaintResponseListDTO> getAllByBusOperatorId(@PathVariable Long busOperatorId) {
        return ApiResponse.success(complaintService.getAllByBusOperatorId(busOperatorId));
    }

    @GetMapping("/agent/{agentId}")
    public ApiResponse<List<ComplaintResponseDetailDTO>> getAllComplaintsByAgentId(@PathVariable Long agentId) {
        return ApiResponse.success(complaintService.getAllComplaintsByAgent(agentId));
    }

    @Operation(summary = "Get all complaints by current agent with pagination")
    @GetMapping("/agent")
    public ApiResponse<ComplaintPageResponseDTO> getAllComplaintsByAgent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            return ApiResponse.success("Lấy danh sách khiếu nại của nhân viên thành công",
                    complaintService.findAllByAssignedAgent(pageable));
        } catch (Exception e) {
            return ApiResponse.error(500, "Đã xảy ra lỗi khi lấy danh sách khiếu nại: " + e.getMessage());
        }
    }

    @Operation(summary = "Get in-progress complaints by current agent with pagination")
    @GetMapping("/agent/in-progress")
    public ApiResponse<ComplaintPageResponseDTO> getInProgressComplaintsByAssignedAgent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            return ApiResponse.success("Lấy danh sách khiếu nại đang xử lý thành công",
                    complaintService.findInProgressByAssignedAgent(pageable));
        } catch (Exception e) {
            return ApiResponse.error(500, "Đã xảy ra lỗi khi lấy danh sách khiếu nại đang xử lý: " + e.getMessage());
        }
    }

    @GetMapping("/agent/daily-stats")
    public ApiResponse<ComplaintStatsDTO> getDailyComplaintStatsForCurrentAgent() {
        return ApiResponse.success(complaintService.getDailyComplaintStatsForCurrentAgent());
    }

    @GetMapping("/agent/stats")
    public ApiResponse<Map<String, Long>> getComplaintStatsForCurrentAgent() {
        return ApiResponse.success(complaintService.getComplaintStatsForCurrentAgent());
    }

}
