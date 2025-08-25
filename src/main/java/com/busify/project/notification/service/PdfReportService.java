package com.busify.project.notification.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.busify.project.bus_operator.dto.response.AdminMonthlyReportsResponse;
import com.busify.project.bus_operator.dto.response.MonthlyBusOperatorReportDTO;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PdfReportService {
    public byte[] generateMonthlyReportPdf(AdminMonthlyReportsResponse response) {
        try {
            String htmlContext = generateHtmlForPdf(response);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ConverterProperties converterProperties = new ConverterProperties();
            // Configure converter properties if needed
            HtmlConverter.convertToPdf(htmlContext, outputStream, converterProperties);

            log.info("✅ Đã tạo PDF báo cáo tháng {}/{}", response.getMonth(), response.getYear());
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("❌ Lỗi khi tạo PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể tạo PDF báo cáo", e);
        }
    }

    private String generateHtmlForPdf(AdminMonthlyReportsResponse report) {
        String monthName = Month.of(report.getMonth())
                .getDisplayName(TextStyle.FULL, new Locale("vi", "VN"));

        StringBuilder html = new StringBuilder();

        html.append(
                """
                        <!DOCTYPE html>
                        <html lang="vi">
                        <head>
                            <meta charset="UTF-8">
                            <title>Báo Cáo Doanh Thu Tháng</title>
                            <style>
                                body { font-family: 'DejaVu Sans', sans-serif; margin: 20px; color: #333; }
                                .header { text-align: center; margin-bottom: 30px; padding: 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border-radius: 10px; }
                                .header h1 { margin: 0; font-size: 24px; }
                                .summary { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; margin-bottom: 30px; }
                                .summary-card { padding: 20px; border: 2px solid #e1e8ed; border-radius: 8px; text-align: center; }
                                .summary-card .number { font-size: 28px; font-weight: bold; color: #2c3e50; margin-bottom: 5px; }
                                .summary-card .label { color: #7f8c8d; font-size: 14px; }
                                .operators-section { margin-top: 30px; }
                                .operators-title { font-size: 18px; color: #2c3e50; margin-bottom: 15px; border-bottom: 2px solid #3498db; padding-bottom: 5px; }
                                .operator-table { width: 100%; border-collapse: collapse; margin-top: 15px; }
                                .operator-table th, .operator-table td { padding: 10px; text-align: left; border: 1px solid #ddd; }
                                .operator-table th { background-color: #34495e; color: white; font-weight: bold; }
                                .operator-table tr:nth-child(even) { background-color: #f8f9fa; }
                                .revenue { color: #27ae60; font-weight: bold; }
                                .footer { margin-top: 30px; text-align: center; font-size: 12px; color: #7f8c8d; }
                            </style>
                        </head>
                        <body>
                            <div class="header">
                                <h1>🚌 BÁO CÁO DOANH THU HỆ THỐNG BUSIFY</h1>
                                <p>""")
                .append(monthName).append(" ").append(report.getYear()).append("""
                            </p>
                        </div>

                        <div class="summary">
                            <div class="summary-card">
                                <div class="number">""").append(formatCurrency(report.getTotalSystemRevenue()))
                .append("""
                            </div>
                            <div class="label">💰 Tổng Doanh Thu</div>
                        </div>
                        <div class="summary-card">
                            <div class="number">""").append(report.getTotalOperators()).append("""
                            </div>
                            <div class="label">🏢 Nhà Xe Hoạt Động</div>
                        </div>
                        <div class="summary-card">
                            <div class="number">""").append(report.getTotalTrips()).append("""
                            </div>
                            <div class="label">🚍 Tổng Chuyến Xe</div>
                        </div>
                        <div class="summary-card">
                            <div class="number">""").append(report.getTotalPassengers()).append("""
                                    </div>
                                    <div class="label">👥 Tổng Hành Khách</div>
                                </div>
                            </div>

                            <div class="operators-section">
                                <h2 class="operators-title">📈 Chi Tiết Theo Nhà Xe</h2>
                                <table class="operator-table">
                                    <thead>
                                        <tr>
                                            <th>STT</th>
                                            <th>Nhà Xe</th>
                                            <th>Email</th>
                                            <th>Chuyến Xe</th>
                                            <th>Hành Khách</th>
                                            <th>Doanh Thu</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                        """);

        // Thêm dữ liệu các nhà xe
        int index = 1;
        for (MonthlyBusOperatorReportDTO operator : report.getOperatorReports()) {
            html.append("""
                    <tr>
                        <td>""").append(index++).append("""
                    </td>
                    <td>""").append(operator.getOperatorName()).append("""
                    </td>
                    <td>""").append(operator.getOperatorEmail()).append("""
                    </td>
                    <td>""").append(operator.getTotalTrips()).append("""
                    </td>
                    <td>""").append(operator.getTotalPassengers()).append("""
                    </td>
                    <td class="revenue">""").append(formatCurrency(operator.getTotalRevenue())).append("""
                                    </td>
                                </tr>
                    """);
        }

        html.append("""
                        </tbody>
                    </table>
                </div>

                <div class="footer">
                    <p>📧 Báo cáo được tạo tự động bởi hệ thống Busify</p>
                    <p>🕐 Tạo lúc: """).append(java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("""
                                </p>
                            </div>
                        </body>
                        </html>
                        """);

        return html.toString();
    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null)
            return "0 VNĐ";
        return String.format("%,.0f VNĐ", amount);
    }
}
