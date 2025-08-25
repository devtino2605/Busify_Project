package com.busify.project.bus_operator.dto.response;

import java.math.BigDecimal;
import java.sql.Date;

public class MonthlyBusOperatorReportDTOImpl implements MonthlyBusOperatorReportDTO {
    private final Long operatorId;
    private final String operatorName;
    private final String operatorEmail;
    private final Long month;
    private final Long year;
    private final Long totalTrips;
    private final BigDecimal totalRevenue;
    private final Long totalPassengers;
    private final Long totalBuses;
    private final Date reportGeneratedDate;
    private final Integer sentToAdmin;

    public MonthlyBusOperatorReportDTOImpl(Long operatorId, String operatorName, String operatorEmail, Long month,
            Long year,
            Long totalTrips, BigDecimal totalRevenue, Long totalPassengers, Long totalBuses,
            Date reportGeneratedDate, Integer sentToAdmin) {
        this.operatorId = operatorId;
        this.operatorName = operatorName;
        this.operatorEmail = operatorEmail;
        this.month = month;
        this.year = year;
        this.totalTrips = totalTrips;
        this.totalRevenue = totalRevenue;
        this.totalPassengers = totalPassengers;
        this.totalBuses = totalBuses;
        this.reportGeneratedDate = reportGeneratedDate;
        this.sentToAdmin = sentToAdmin;
    }

    @Override
    public Long getOperatorId() {
        return operatorId;
    }

    @Override
    public String getOperatorName() {
        return operatorName;
    }

    @Override
    public String getOperatorEmail() {
        return operatorEmail;
    }

    @Override
    public Long getMonth() {
        return month;
    }

    @Override
    public Long getYear() {
        return year;
    }

    @Override
    public Long getTotalTrips() {
        return totalTrips;
    }

    @Override
    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    @Override
    public Long getTotalPassengers() {
        return totalPassengers;
    }

    @Override
    public Long getTotalBuses() {
        return totalBuses;
    }

    @Override
    public Date getReportGeneratedDate() {
        return reportGeneratedDate;
    }

    @Override
    public Integer getSentToAdmin() {
        return sentToAdmin;
    }
}
