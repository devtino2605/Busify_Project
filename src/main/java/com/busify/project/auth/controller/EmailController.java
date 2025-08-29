package com.busify.project.auth.controller;

import com.busify.project.auth.dto.request.CustomerSupportEmailRequestDTO;
import com.busify.project.auth.service.CustomerServiceSendMail;
import com.busify.project.common.dto.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final CustomerServiceSendMail emailService;

    @PostMapping("/customer-support")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER_SERVICE') or hasRole('OPERATOR')")
    public ApiResponse<?> sendCustomerSupportEmail(
            @Valid @RequestBody CustomerSupportEmailRequestDTO request) {
        try {
            // Send the email
            emailService.sendCustomerSupportEmail(
                    request.getToEmail(),
                    request.getUserName(),
                    request.getSubject(),
                    request.getMessage(),
                    request.getCaseNumber(),
                    request.getCsRepName());

            return ApiResponse.success("Email hỗ trợ khách hàng đã được gửi thành công", null);
        } catch (Exception e) {
            return ApiResponse.error(500, "Gửi email thất bại: " + e.getMessage());
        }
    }

    // @PostMapping("/simple")
    // @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER_SERVICE')")
    // public ResponseEntity<ApiResponse<?>> sendSimpleEmail(
    // @RequestParam String toEmail,
    // @RequestParam String subject,
    // @RequestParam String content) {

    // try {

    // emailService.sendSimpleEmail(toEmail, subject, content);

    // return ResponseEntity.ok(
    // ApiResponse.success("Email đã được gửi thành công", null));

    // } catch (Exception e) {

    // return ResponseEntity.internalServerError().body(
    // ApiResponse.error(500, "Gửi email thất bại: " + e.getMessage()));
    // }
    // }
}
