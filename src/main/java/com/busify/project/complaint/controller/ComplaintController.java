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


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintServiceImpl complaintService;

    @GetMapping("/{id}")
    public ComplaintResponseDTO getComplaintById(@PathVariable Long id) {
        return complaintService.getComplaintById(id);
    }

    @GetMapping("/bookings/{bookingId}")
    public ComplaintResponseListDTO getAllComplaints(@PathVariable Long bookingId) {
        return complaintService.getAllComplaintsByBooking(bookingId);
    }

    @GetMapping("/customer/{customerId}")
    public ComplaintResponseListDTO getAllComplaintsByCustomer(@PathVariable Long customerId) {
        return complaintService.getAllComplaintsByCustomer(customerId);
    }

    @PostMapping("/booking/{bookingId}")
    public ComplaintResponseDTO addComplaint(@PathVariable Long bookingId, @RequestBody ComplaintAddDTO complaintAddDTO) {
        return complaintService.addComplaint(complaintAddDTO);
    }
    
    @DeleteMapping("/{id}")
    public void deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
    }

    @PatchMapping("/{id}")
    public ComplaintResponseDTO updateComplaint(@PathVariable Long id, @RequestBody ComplaintUpdateDTO complaintUpdateDTO) {
        return complaintService.updateComplaint(id, complaintUpdateDTO);
    }

}
