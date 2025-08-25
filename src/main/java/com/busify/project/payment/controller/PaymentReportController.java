package com.busify.project.payment.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.payment.service.impl.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report/payments")
@RequiredArgsConstructor
public class PaymentReportController {

    private final PaymentServiceImpl paymentService;

    @GetMapping("{operatorId}")
    public ApiResponse<?> getAllPaymentsByOperator(@PathVariable String operatorId) {
        return ApiResponse.success("");
    }
}
