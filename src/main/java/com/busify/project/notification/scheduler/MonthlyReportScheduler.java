package com.busify.project.notification.scheduler;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.busify.project.bus_operator.dto.response.AdminMonthlyReportsResponse;
import com.busify.project.bus_operator.service.BusOperatorService;
import com.busify.project.notification.service.MonthlyReportNotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MonthlyReportScheduler {
    private final BusOperatorService busOperatorService;
    private final MonthlyReportNotificationService notificationService;

    @Value("${busify.admin.user-email}")
    private String adminUserEmail;

    // Chạy vào ngày 1 hàng tháng lúc 8:00 AM
    @Scheduled(cron = "0 0 8 1 * ?")
    public void generateAndSendMonthlyReportNotification() {
        try {
            LocalDate lastMonth = LocalDate.now().minusMonths(1);
            int month = lastMonth.getMonthValue();
            int year = lastMonth.getYear();

            log.info("🤖 Bắt đầu tạo notification báo cáo tự động cho tháng {}/{}", month, year);

            // Tạo báo cáo tháng trước
            AdminMonthlyReportsResponse report = busOperatorService.getAllMonthlyReports(month, year);

            // Tạo notification thay vì gửi email
            notificationService.createMonthlyReportNotification(adminUserEmail, report);

            log.info("🔔 Đã tạo notification báo cáo tháng {}/{} cho admin email: {}", month, year, adminUserEmail);

            // Đánh dấu đã xử lý
            busOperatorService.markReportAsSent(month, year);

        } catch (Exception e) {
            log.error("❌ Lỗi khi tạo notification báo cáo tự động: {}", e.getMessage(), e);
        }
    }

    // Test notification
    public void generateTestNotification() {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();

        log.info("🧪 Tạo test notification cho tháng hiện tại {}/{}", month, year);

        try {
            AdminMonthlyReportsResponse report = busOperatorService.getAllMonthlyReports(month, year);
            notificationService.createMonthlyReportNotification(adminUserEmail, report);

            log.info("✅ Đã tạo test notification thành công");
        } catch (Exception e) {
            log.error("❌ Lỗi khi tạo test notification: {}", e.getMessage(), e);
        }
    }
}