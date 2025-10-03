package com.busify.project.notification.controller;

import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.common.service.FileStorageService;
import com.busify.project.notification.dto.NotificationDTO;
import com.busify.project.notification.scheduler.MonthlyReportScheduler;
import com.busify.project.notification.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Admin Notifications", description = "Admin Notification Management API")
public class AdminController {
    private final MonthlyReportScheduler monthlyReportScheduler;
    private final NotificationService notificationService;
    private final FileStorageService fileStorageService;

    @Operation(summary = "Generate test monthly notification")
    @PostMapping("/generate-monthly-notification")
    public ApiResponse<String> generateTestMonthlyNotification() {
        try {
            monthlyReportScheduler.generateTestNotification();
            return ApiResponse.success("📱 Test notification đã được tạo thành công!", null);
        } catch (Exception e) {
            return ApiResponse.error(500, "Lỗi khi tạo notification: " + e.getMessage());
        }
    }

    @Operation(summary = "Download report PDF")
    @GetMapping("/reports/download/{notificationId}")
    public ResponseEntity<byte[]> downloadReportPdf(@PathVariable Long notificationId) {
        try {
            // Lấy notification để có metadata (PDF path)
            NotificationDTO notification = notificationService.getNotificationById(notificationId);
            String pdfPath = notification.getMetadata();

            byte[] pdfContent = fileStorageService.readPdfReport(pdfPath);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", Paths.get(pdfPath).getFileName().toString());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfContent);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get all notifications")
    @GetMapping
    public ApiResponse<List<NotificationDTO>> getMyNotifications() {

        List<NotificationDTO> notifications = notificationService.getNotificationsByUser();
        return ApiResponse.success("Lấy notifications thành công", notifications);
    }

    @Operation(summary = "Get unread notifications")
    // Lấy notifications chưa đọc
    @GetMapping("/unread")
    public ApiResponse<List<NotificationDTO>> getUnreadNotifications() {
        List<NotificationDTO> notifications = notificationService.getUnreadNotifications();
        return ApiResponse.success("Lấy notifications chưa đọc thành công", notifications);
    }

    @Operation(summary = "Count unread notifications")
    // Đếm notifications chưa đọc
    @GetMapping("/unread/count")
    public ApiResponse<Long> countUnreadNotifications() {
        long count = notificationService.countUnreadNotifications();

        return ApiResponse.success("Đếm notifications chưa đọc thành công", count);
    }

    @Operation(summary = "Mark notification as read")
    // Đánh dấu đã đọc
    @PutMapping("/{id}/read")
    public ApiResponse<NotificationDTO> markAsRead(@PathVariable Long id) {
        NotificationDTO notification = notificationService.markAsRead(id);

        return ApiResponse.success("Đánh dấu đã đọc thành công", notification);
    }

    @Operation(summary = "Mark all notifications as read")
    // Đánh dấu tất cả đã đọc
    @PutMapping("/read-all")
    public ApiResponse<String> markAllAsRead() {
        notificationService.markAllAsRead();

        return ApiResponse.success("Đánh dấu tất cả đã đọc thành công", null);
    }

    @Operation(summary = "Delete notification")
    // Xóa notification
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);

        return ApiResponse.success("Xóa notification thành công", null);
    }

    @Operation(summary = "Generate monthly report manually")
    @PostMapping("/generate-report")
    public ApiResponse<String> generateMonthlyReport() {
        try {
            monthlyReportScheduler.generateAndSendMonthlyReportNotification();
            return ApiResponse.success("Generate report successfully", "Monthly report generated successfully");
        } catch (Exception e) {
            return ApiResponse.internalServerError("Error generating report: " + e.getMessage());
        }
    }
}