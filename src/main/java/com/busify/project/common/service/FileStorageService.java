package com.busify.project.common.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.itextpdf.io.exceptions.IOException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileStorageService {
    @Value("${busify.reports.storage.path}")
    private String reportsStoragePath;

    public String savePdfReport(byte[] pdfContent, int month, int year) {
        try {
            Path storageDir = Paths.get(reportsStoragePath + "/reports");
            if (!Files.exists(storageDir)) {
                Files.createDirectories(storageDir);
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = String.format("monthly_report_%d_%02d_%s.pdf", year, month, timestamp);
            Path filePath = storageDir.resolve(fileName);
            Files.write(filePath, pdfContent);

            log.info("✅ Đã lưu PDF báo cáo: {}", filePath.toString());

            return filePath.toString();
        } catch (Exception e) {
            log.error("Error saving PDF report", e);
            return null;
        }
    }

    public byte[] readPdfReport(String filePath) throws java.io.IOException {
        try {
            Path path = Paths.get(filePath);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            log.error("❌ Lỗi khi đọc PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể đọc PDF báo cáo", e);
        }
    }
}
