package com.busify.project.payment.controller;

import com.busify.project.common.dto.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Report", description = "Payment Report API")
public class PaymentReportController {

    @GetMapping("{operatorId}")
    @Operation(summary = "Get all payments by operator")
    public ApiResponse<?> getAllPaymentsByOperator(@PathVariable String operatorId) {
        return ApiResponse.success("");
    }
}
