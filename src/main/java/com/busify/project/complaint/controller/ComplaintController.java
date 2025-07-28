package com.busify.project.complaint.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busify.project.complaint.dto.response.ComplaintResponseDTO;
import com.busify.project.complaint.service.ComplaintServiceImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintServiceImpl complaintService;

    @GetMapping("/{id}")
    public ComplaintResponseDTO getComplaintById(@RequestParam Long id) {
        return complaintService.getComplaintById(id);
    }

}
