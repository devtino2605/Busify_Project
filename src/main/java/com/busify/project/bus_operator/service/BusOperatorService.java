package com.busify.project.bus_operator.service;

import com.busify.project.bus_operator.dto.request.BusOperatorFilterRequest;
import com.busify.project.bus_operator.dto.request.CreateBusOperatorRequest;
import com.busify.project.bus_operator.dto.request.UpdateBusOperatorRequest;
import com.busify.project.bus_operator.dto.response.*;

import java.util.List;

public interface BusOperatorService {
    List<BusOperatorFilterTripResponse> getAllBusOperators();

    List<BusOperatorRatingResponse> getAllBusOperatorsByRating(Integer limit);

    BusOperatorDetailsResponse getOperatorById(Long id);

    List<BusOperatorResponse> getAllActiveOperators();

    BusOperatorManagementPageResponse getBusOperatorsForManagement(BusOperatorFilterRequest filterRequest);

    BusOperatorForManagement createBusOperator(CreateBusOperatorRequest request);

    BusOperatorForManagement updateBusOperator(Long id, UpdateBusOperatorRequest request);

    void deleteBusOperator(Long id);

    BusOperatorForManagement getBusOperatorForManagementById(Long id);

    MonthlyBusOperatorReportDTO getMonthlyReportByOperatorId(Long operatorId, int month, int year);

    WeeklyBusOperatorReportDTO getWeeklyReportByOperatorId(Long operatorId);

    BusOperatorResponse getOperatorDetailByUser();

    // Admin xem tất cả báo cáo
    AdminMonthlyReportsResponse getAllMonthlyReports(int month, int year);

    // Lấy báo cáo tháng hiện tại
    MonthlyBusOperatorReportDTO getCurrentMonthReport(Long operatorId);

    // Lấy tổng doanh thu của tất cả bus operators theo từng tháng trong năm
    List<MonthlyTotalRevenueDTO> getMonthlyTotalRevenueByYear(int year);

    public void markReportAsSent(int month, int year);

}
