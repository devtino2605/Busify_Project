package com.busify.project.common.config;

import com.busify.project.complaint.service.ComplaintAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Autowired
    private ComplaintAssignmentService assignmentService;

    // Chạy mỗi 5 giây
    @Scheduled(fixedRate = 10000)
    public void scheduleComplaintAssignment() {
        try {
            System.out.println("=== Bắt đầu kiểm tra khiếu nại mới ===");
            Optional<com.busify.project.complaint.entity.Complaint> result = assignmentService
                    .assignComplaintToAvailableAgent();

            if (result.isPresent()) {
                System.out.println("✓ Đã gán khiếu nại thành công");
            } else {
                System.out.println("○ Không có khiếu nại nào được gán");
            }
            System.out.println("=== Kết thúc kiểm tra ===\n");
        } catch (Exception e) {
            System.err.println("❌ Lỗi trong quá trình gán khiếu nại: " + e.getMessage());
            e.printStackTrace();
        }
    }
}