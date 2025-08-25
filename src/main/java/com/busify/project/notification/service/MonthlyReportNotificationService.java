// src/main/java/com/busify/project/notification/service/MonthlyReportNotificationService.java
package com.busify.project.notification.service;

import com.busify.project.bus_operator.dto.response.AdminMonthlyReportsResponse;
import com.busify.project.common.service.FileStorageService;
import com.busify.project.notification.entity.Notification;
import com.busify.project.notification.enums.NotificationType;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MonthlyReportNotificationService {

    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final PdfReportService pdfReportService;
    private final FileStorageService fileStorageService;

    public void createMonthlyReportNotification(String email, AdminMonthlyReportsResponse report) {
        String monthName = Month.of(report.getMonth())
                .getDisplayName(TextStyle.FULL, new Locale("vi", "VN"));

        String title = String.format("📊 Báo Cáo Doanh Thu Tháng %s %d", monthName, report.getYear());

        byte[] pdfContent = pdfReportService.generateMonthlyReportPdf(report);
        String pdfPath = fileStorageService.savePdfReport(pdfContent, report.getMonth(), report.getYear());

        String message = generateNotificationMessage(report, monthName, pdfPath);

        String actionUrl = String.format("/api/bus-operators/admin/monthly-reports?month=%d&year=%d",
                report.getMonth(), report.getYear());

        String relatedId = String.format("%d-%02d", report.getYear(), report.getMonth());
        Optional<User> user = userRepository.findByEmail(email);

        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .type(NotificationType.MONTHLY_REPORT)
                .userId(user.map(User::getId).orElse(null))
                .relatedId(relatedId)
                .actionUrl(actionUrl)
                .metaData(pdfPath)
                .build();

        notificationService.createNotification(notification);

    }

    private String generateNotificationMessage(AdminMonthlyReportsResponse report, String monthName, String pdfPath) {
        return String.format(
                "💰 Tổng doanh thu: %s <br>" +
                        "🏢 Nhà xe hoạt động: %d<br>" +
                        "🚍 Tổng chuyến xe: %d<br>" +
                        "👥 Tổng hành khách: %d<br><br>" +
                        "📈 Nhà xe doanh thu cao nhất: %s (%s)<br><br>" +
                        "📄 Tệp đính kèm: %s<br><br>" +
                        "👆 Nhấn để xem báo cáo chi tiết",
                formatCurrency(report.getTotalSystemRevenue()),
                report.getTotalOperators(),
                report.getTotalTrips(),
                report.getTotalPassengers(),
                getTopOperatorName(report),
                formatCurrency(getTopOperatorRevenue(report)),
                Paths.get(pdfPath).getFileName().toString());
    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null)
            return "0 VNĐ";
        return String.format("%,.0f VNĐ", amount);
    }

    private String getTopOperatorName(AdminMonthlyReportsResponse report) {
        return report.getOperatorReports().stream()
                .findFirst()
                .map(op -> op.getOperatorName())
                .orElse("N/A");
    }

    private BigDecimal getTopOperatorRevenue(AdminMonthlyReportsResponse report) {
        return report.getOperatorReports().stream()
                .findFirst()
                .map(op -> op.getTotalRevenue())
                .orElse(BigDecimal.ZERO);
    }
}