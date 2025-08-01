package com.busify.project.complaint.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busify.project.complaint.dto.ComplaintAddDTO;
import com.busify.project.complaint.dto.ComplaintUpdateDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseListDTO;
import com.busify.project.complaint.service.ComplaintServiceImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.busify.project.common.dto.response.ApiResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintServiceImpl complaintService;

    @GetMapping("/{id}")
    public ApiResponse<ComplaintResponseDTO> getComplaintById(@PathVariable Long id) {
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

    @GetMapping("/trip/{tripId}")
    public ApiResponse<ComplaintResponseListDTO> getAllComplaintsByTrip(@PathVariable Long tripId) {
        return ApiResponse.success(complaintService.getAllComplaintsByTrip(tripId));
    }

    @PostMapping("/booking/{bookingId}")
    public ApiResponse<ComplaintResponseDTO> addComplaint(@PathVariable Long bookingId,
            @RequestBody ComplaintAddDTO complaintAddDTO) {
        return ApiResponse.success(complaintService.addComplaint(complaintAddDTO));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
        return ApiResponse.success("Complaint deleted successfully", null);
    }

    @PatchMapping("/{id}")
    public ApiResponse<ComplaintResponseDTO> updateComplaint(@PathVariable Long id,
            @RequestBody ComplaintUpdateDTO complaintUpdateDTO) {
        return ApiResponse.success(complaintService.updateComplaint(id, complaintUpdateDTO));
    }

    @GetMapping("/bus-operator/{busOperatorId}")
    public ApiResponse<ComplaintResponseListDTO> getAllByBusOperatorId(@PathVariable Long busOperatorId) {
        return ApiResponse.success(complaintService.getAllByBusOperatorId(busOperatorId));
    }
}
