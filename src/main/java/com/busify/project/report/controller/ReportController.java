package com.busify.project.report.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busify.project.common.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import com.busify.project.report.service.ReportService;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/yearly")
    public ApiResponse<?> getYearlyReport(@RequestParam Long operatorId, @RequestParam Integer year) {
        return ApiResponse.success("Get report successfully", reportService.getReportsByYear(year, operatorId));
    }
    
    @GetMapping("/year-range")
    public ApiResponse<?> getYearRangeReport(@RequestParam Integer start, @RequestParam Integer end,
            @RequestParam Long operatorId) {
        return ApiResponse.success("Get report successfully",
                reportService.getReportsByYearRange(start, end, operatorId));
    }
}
