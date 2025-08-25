package com.busify.project.bus_operator.controller;

import com.busify.project.booking.dto.response.BookingStatusCountDTO;
import com.busify.project.bus_operator.dto.request.BusOperatorFilterRequest;
import com.busify.project.bus_operator.dto.request.CreateBusOperatorRequest;
import com.busify.project.bus_operator.dto.request.UpdateBusOperatorRequest;
import com.busify.project.bus_operator.dto.response.*;
import com.busify.project.bus_operator.service.BusOperatorService;
import com.busify.project.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/bus-operators")
@RequiredArgsConstructor
public class BusOperatorController {

        private final BusOperatorService busOperatorService;

        @GetMapping("/rating")
        public ApiResponse<List<BusOperatorRatingResponse>> getAllBusOperatorsByRatingWithLimit(
                        @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
                List<BusOperatorRatingResponse> busOperators = busOperatorService.getAllBusOperatorsByRating(limit);
                return ApiResponse.<List<BusOperatorRatingResponse>>builder()
                                .code(HttpStatus.OK.value())
                                .message("Upcoming trips retrieved successfully")
                                .result(busOperators)
                                .build();
        }

        @GetMapping("/{id}")
        public ApiResponse<BusOperatorDetailsResponse> getOperatorById(@PathVariable Long id) {
                BusOperatorDetailsResponse busOperator = busOperatorService.getOperatorById(id);
                return ApiResponse.<BusOperatorDetailsResponse>builder()
                                .code(HttpStatus.OK.value())
                                .message("Bus operator retrieved successfully")
                                .result(busOperator)
                                .build();
        }

        @GetMapping
        public ApiResponse<List<BusOperatorResponse>> getAllOperators() {
                List<BusOperatorResponse> busOperators = busOperatorService.getAllActiveOperators();
                return ApiResponse.success("All bus operators fetched successfully", busOperators);
        }

        @GetMapping("/management")
        @PreAuthorize("hasRole('ADMIN')")
        public ApiResponse<BusOperatorManagementPageResponse> getBusOperatorsForManagementPaginated(
                        BusOperatorFilterRequest filterRequest) {
                BusOperatorManagementPageResponse response = busOperatorService
                                .getBusOperatorsForManagement(filterRequest);
                return ApiResponse.<BusOperatorManagementPageResponse>builder()
                                .code(HttpStatus.OK.value())
                                .message("Bus operators for management fetched successfully with pagination and filters")
                                .result(response)
                                .build();
        }

        @PostMapping("/management")
        @PreAuthorize("hasRole('ADMIN')")
        public ApiResponse<BusOperatorForManagement> createBusOperator(
                        @Valid @ModelAttribute CreateBusOperatorRequest request) {
                BusOperatorForManagement busOperator = busOperatorService.createBusOperator(request);
                return ApiResponse.<BusOperatorForManagement>builder()
                                .code(HttpStatus.CREATED.value())
                                .message("Bus operator created successfully")
                                .result(busOperator)
                                .build();
        }

        @GetMapping("/management/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ApiResponse<BusOperatorForManagement> getBusOperatorForManagement(@PathVariable Long id) {
                BusOperatorForManagement busOperator = busOperatorService.getBusOperatorForManagementById(id);
                return ApiResponse.<BusOperatorForManagement>builder()
                                .code(HttpStatus.OK.value())
                                .message("Bus operator retrieved successfully")
                                .result(busOperator)
                                .build();
        }

        @PutMapping("/management/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ApiResponse<BusOperatorForManagement> updateBusOperator(
                        @PathVariable Long id,
                        @Valid @ModelAttribute UpdateBusOperatorRequest request) {
                BusOperatorForManagement busOperator = busOperatorService.updateBusOperator(id, request);
                return ApiResponse.<BusOperatorForManagement>builder()
                                .code(HttpStatus.OK.value())
                                .message("Bus operator updated successfully")
                                .result(busOperator)
                                .build();
        }

        @DeleteMapping("/management/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ApiResponse<Void> deleteBusOperator(@PathVariable Long id) {
                busOperatorService.deleteBusOperator(id);
                return ApiResponse.<Void>builder()
                                .code(HttpStatus.OK.value())
                                .message("Bus operator deleted successfully")
                                .build();
        }

        @GetMapping("/{id}/report")
        public ApiResponse<WeeklyBusOperatorReportDTO> getWeeklyReportByOperatorId(@PathVariable Long id) {
                WeeklyBusOperatorReportDTO report = busOperatorService.getWeeklyReportByOperatorId(id);
                return ApiResponse.<WeeklyBusOperatorReportDTO>builder()
                                .code(HttpStatus.OK.value())
                                .message("Weekly report retrieved successfully")
                                .result(report)
                                .build();
        }

        @GetMapping("/user")
        public ApiResponse<BusOperatorResponse> getOperatorInfo() {
                BusOperatorResponse operatorInfo = busOperatorService.getOperatorDetailByUser();
                return ApiResponse.<BusOperatorResponse>builder()
                                .code(HttpStatus.OK.value())
                                .message("Bus operator info retrieved successfully")
                                .result(operatorInfo)
                                .build();
        }

        @GetMapping("/{id}/monthly-report")
        public ApiResponse<MonthlyBusOperatorReportDTO> getMonthlyReportByOperatorId(
                        @PathVariable Long id,
                        @RequestParam(value = "month", required = false) Integer month,
                        @RequestParam(value = "year", required = false) Integer year) {

                LocalDate now = LocalDate.now();
                int reportMonth = (month != null) ? month : now.getMonthValue();
                int reportYear = (year != null) ? year : now.getYear();

                MonthlyBusOperatorReportDTO report = busOperatorService.getMonthlyReportByOperatorId(id, reportMonth,
                                reportYear);
                return ApiResponse.<MonthlyBusOperatorReportDTO>builder()
                                .code(HttpStatus.OK.value())
                                .message("Monthly report retrieved successfully")
                                .result(report)
                                .build();
        }

        @GetMapping("/user/monthly-report")
        public ApiResponse<MonthlyBusOperatorReportDTO> getCurrentMonthReportForUser() {
                // Lấy operator ID từ user hiện tại
                BusOperatorResponse operatorInfo = busOperatorService.getOperatorDetailByUser();
                MonthlyBusOperatorReportDTO report = busOperatorService.getCurrentMonthReport(operatorInfo.getId());

                return ApiResponse.<MonthlyBusOperatorReportDTO>builder()
                                .code(HttpStatus.OK.value())
                                .message("Current month report retrieved successfully")
                                .result(report)
                                .build();
        }

        // Admin xem tất cả báo cáo hàng tháng
        @GetMapping("/admin/monthly-reports")
        @PreAuthorize("hasRole('ADMIN')")
        public ApiResponse<AdminMonthlyReportsResponse> getAllMonthlyReports(
                        @RequestParam(value = "month", required = false) Integer month,
                        @RequestParam(value = "year", required = false) Integer year) {

                LocalDate now = LocalDate.now();
                int reportMonth = (month != null) ? month : now.getMonthValue();
                int reportYear = (year != null) ? year : now.getYear();

                AdminMonthlyReportsResponse reports = busOperatorService.getAllMonthlyReports(reportMonth, reportYear);
                return ApiResponse.<AdminMonthlyReportsResponse>builder()
                                .code(HttpStatus.OK.value())
                                .message("Monthly reports retrieved successfully")
                                .result(reports)
                                .build();
        }

        // Admin xem tổng doanh thu các tháng trong năm
        @GetMapping("/admin/yearly-revenue")
        @PreAuthorize("hasRole('ADMIN')")
        public ApiResponse<List<MonthlyTotalRevenueDTO>> getMonthlyTotalRevenueByYear(
                        @RequestParam(value = "year", required = false) Integer year) {

                LocalDate now = LocalDate.now();
                int reportYear = (year != null) ? year : now.getYear();

                List<MonthlyTotalRevenueDTO> monthlyRevenues = busOperatorService
                                .getMonthlyTotalRevenueByYear(reportYear);
                return ApiResponse.<List<MonthlyTotalRevenueDTO>>builder()
                                .code(HttpStatus.OK.value())
                                .message("Yearly revenue by month retrieved successfully")
                                .result(monthlyRevenues)
                                .build();
        }

}