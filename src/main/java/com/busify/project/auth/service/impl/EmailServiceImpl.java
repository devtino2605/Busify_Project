package com.busify.project.auth.service.impl;

import com.busify.project.ticket.entity.Tickets;
import com.busify.project.auth.util.PdfGeneratorUtil;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.busify.project.auth.service.EmailService;
import com.busify.project.common.config.EmailConfig;
import com.busify.project.common.exception.EmailSendException;
import com.busify.project.user.entity.Profile;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final EmailConfig emailConfig;
    private final JavaMailSender mailSender;

    @Override
    @Async("emailExecutor")
    public void sendVerificationEmail(Profile user, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailConfig.getFromEmail());
            helper.setTo(user.getEmail());
            helper.setSubject("Xác thực email của bạn");

            String verificationUrl = emailConfig.getFrontendUrl() + "/verify-email?token=" + token;
            String htmlContent = buildVerificationEmailContent(user.getFullName(), verificationUrl);

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new EmailSendException("Failed to send verification email", e);
        }
    }

    private String buildVerificationEmailContent(String fullName, String verificationUrl) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Xác thực Email</title>
                </head>
                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                    <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                        <h2 style="color: #4CAF50;">Xác thực Email của bạn</h2>
                        <p>Xin chào <strong>%s</strong>,</p>
                        <p>Cảm ơn bạn đã đăng ký tài khoản. Vui lòng click vào link bên dưới để xác thực email:</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%s"
                               style="background-color: #4CAF50; color: white; padding: 12px 30px;
                                      text-decoration: none; border-radius: 5px; display: inline-block;">
                                Xác thực Email
                            </a>
                        </div>
                        <p>Hoặc copy link sau vào trình duyệt:</p>
                        <p style="word-break: break-all; background-color: #f5f5f5; padding: 10px; border-radius: 3px;">
                            %s
                        </p>
                        <p><strong>Lưu ý:</strong> Link này sẽ hết hạn sau 24 giờ.</p>
                        <p>Nếu bạn không đăng ký tài khoản này, vui lòng bỏ qua email này.</p>
                        <hr style="margin: 30px 0;">
                        <p style="font-size: 12px; color: #666;">
                            Email này được gửi tự động, vui lòng không reply.
                        </p>
                    </div>
                </body>
                </html>
                """.formatted(fullName, verificationUrl, verificationUrl);
    }

    @Override
    @Async("emailExecutor")
    public void sendPasswordResetEmail(Profile user, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailConfig.getFromEmail());
            helper.setTo(user.getEmail());
            helper.setSubject("Đặt lại mật khẩu");

            String resetUrl = emailConfig.getFrontendUrl() + "/reset-password?token=" + token;
            String htmlContent = buildPasswordResetEmailContent(user.getFullName(), resetUrl);

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new EmailSendException("Failed to send password reset email", e);
        }
    }

    private String buildPasswordResetEmailContent(String fullName, String resetUrl) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Đặt lại mật khẩu</title>
                </head>
                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                    <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                        <h2 style="color: #FF6B6B;">Đặt lại mật khẩu</h2>
                        <p>Xin chào <strong>%s</strong>,</p>
                        <p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn.</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%s"
                               style="background-color: #FF6B6B; color: white; padding: 12px 30px;
                                      text-decoration: none; border-radius: 5px; display: inline-block;">
                                Đặt lại mật khẩu
                            </a>
                        </div>
                        <p>Hoặc copy link sau vào trình duyệt:</p>
                        <p style="word-break: break-all; background-color: #f5f5f5; padding: 10px; border-radius: 3px;">
                            %s
                        </p>
                        <p><strong>Lưu ý:</strong> Link này sẽ hết hạn sau 24 giờ.</p>
                        <p>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>
                    </div>
                </body>
                </html>
                """.formatted(fullName, resetUrl, resetUrl);
    }

    @Override
    public void sendTicketEmail(String toEmail, String fullName, List<Tickets> tickets) {
        System.out.println("DEBUG EmailService: Starting sendTicketEmail");
        System.out.println("DEBUG EmailService: To email: " + toEmail);
        System.out.println("DEBUG EmailService: Full name: " + fullName);
        System.out.println("DEBUG EmailService: Number of tickets: " + tickets.size());

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailConfig.getFromEmail());
            helper.setTo(toEmail);
            helper.setSubject("Xác nhận đặt vé của bạn");

            String htmlContent = buildTicketEmailContent(fullName, tickets);
            helper.setText(htmlContent, true);

            // Tạo và đính kèm file PDF using the utility
            byte[] pdfBytes = PdfGeneratorUtil.generateTicketPDF(fullName, tickets);
            helper.addAttachment("ve-xe-busify.pdf", new ByteArrayResource(pdfBytes));

            System.out.println("DEBUG EmailService: About to send email...");
            mailSender.send(message);
            System.out.println("DEBUG EmailService: Email sent successfully!");

        } catch (MessagingException | IOException e) {
            System.err.println("DEBUG EmailService: Failed to send email: " + e.getMessage());
            e.printStackTrace();
            throw new EmailSendException("Failed to send ticket email", e);
        }
    }

    private String buildTicketEmailContent(String fullName, List<Tickets> tickets) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
                .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        NumberFormat currencyFormatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        StringBuilder ticketCards = new StringBuilder();

        for (Tickets ticket : tickets) {
            String departureTime = formatter.format(ticket.getBooking().getTrip().getDepartureTime());
            String arrivalTime = formatter.format(ticket.getBooking().getTrip().getEstimatedArrivalTime());
            String formattedPrice = currencyFormatter.format(ticket.getPrice());

            ticketCards
                    .append("""
                            <div style="border: 2px dashed #4CAF50; border-radius: 10px; padding: 15px; margin-bottom: 20px; background-color: #f9fff9;">
                                <h3 style="margin: 0; color: #4CAF50;">🎫 Mã vé: %s</h3>
                                <p style="margin: 5px 0;"><strong>Số ghế:</strong> %s</p>
                                <p style="margin: 5px 0;"><strong>Giá:</strong> %s VND</p>
                                <p style="margin: 5px 0;"><strong>Giờ khởi hành:</strong> %s</p>
                                <p style="margin: 5px 0;"><strong>Giờ đến dự kiến:</strong> %s</p>
                                <p style="margin: 5px 0;"><strong>Điểm đi:</strong> %s</p>
                                <p style="margin: 5px 0;"><strong>Điểm đến:</strong> %s</p>
                                <p style="margin: 5px 0;"><strong>Biển số xe:</strong> %s</p>
                            </div>
                            """
                            .formatted(
                                    ticket.getTicketCode(),
                                    ticket.getSeatNumber(),
                                    formattedPrice,
                                    departureTime,
                                    arrivalTime,
                                    ticket.getBooking().getTrip().getRoute().getStartLocation().getName(),
                                    ticket.getBooking().getTrip().getRoute().getEndLocation().getName(),
                                    ticket.getBooking().getTrip().getBus().getLicensePlate()));
        }

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Vé đặt thành công</title>
                </head>
                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333; background-color: #f5f5f5; padding: 20px;">
                    <div style="max-width: 600px; margin: 0 auto; background: white; padding: 20px; border-radius: 10px;">
                        <h2 style="color: #4CAF50;">Xin chào %s,</h2>
                        <p>Cảm ơn bạn đã đặt vé tại <strong>Busify</strong>. Dưới đây là thông tin vé của bạn:</p>
                        %s
                        <p style="margin-top: 20px;"><strong>📎 File PDF với QR code đã được đính kèm trong email này.</strong></p>
                        <p>Chúc bạn có chuyến đi an toàn và vui vẻ! 🚌</p>
                        <p style="font-size: 12px; color: #666;">Email này được gửi tự động, vui lòng không trả lời.</p>
                    </div>
                </body>
                </html>
                """
                .formatted(fullName, ticketCards.toString());
    }

    @Override
    @Async("emailExecutor")
    public void sendSimpleEmail(String toEmail, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailConfig.getFromEmail());
            helper.setTo(toEmail);
            helper.setSubject(subject);

            // Simple HTML wrapper for the content
            String htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <title>%s</title>
                    </head>
                    <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                        <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                            %s
                        </div>
                    </body>
                    </html>
                    """.formatted(subject, content.replace("\n", "<br>"));

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new EmailSendException("Failed to send simple email", e);
        }
    }

    @Override
    @Async("emailExecutor")
    public void sendTicketCancelledEmail(String toEmail, String fullName, Tickets ticket) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailConfig.getFromEmail());
            helper.setTo(toEmail);
            helper.setSubject("Thông báo hủy vé");

            String htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <title>Vé bị hủy</title>
                    </head>
                    <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                        <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                            <h2 style="color: #FF6B6B;">Vé của bạn đã bị hủy</h2>
                            <p>Xin chào <strong>%s</strong>,</p>
                            <p>Vé với mã <strong>%s</strong> đã bị hủy. Nếu bạn có thắc mắc, vui lòng liên hệ hỗ trợ.</p>
                            <p style="font-size: 12px; color: #666;">Email này được gửi tự động, vui lòng không trả lời.</p>
                        </div>
                    </body>
                    </html>
                    """
                    .formatted(fullName, ticket.getTicketCode());

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new EmailSendException("Failed to send ticket cancelled email", e);
        }
    }

    @Override
    @Async("emailExecutor")
    public void sendBookingCancelledEmail(String toEmail, String fullName, List<Tickets> tickets) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailConfig.getFromEmail());
            helper.setTo(toEmail);
            helper.setSubject("Thông báo hủy booking");

            StringBuilder ticketList = new StringBuilder();
            for (Tickets ticket : tickets) {
                ticketList.append("<li>Mã vé: ").append(ticket.getTicketCode())
                        .append(", Số ghế: ").append(ticket.getSeatNumber()).append("</li>");
            }

            String htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <title>Booking bị hủy</title>
                    </head>
                    <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                        <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                            <h2 style="color: #FF6B6B;">Booking của bạn đã bị hủy</h2>
                            <p>Xin chào <strong>%s</strong>,</p>
                            <p>Booking của bạn đã bị hủy. Danh sách vé:</p>
                            <ul>%s</ul>
                            <p>Nếu bạn có thắc mắc, vui lòng liên hệ hỗ trợ.</p>
                            <p style="font-size: 12px; color: #666;">Email này được gửi tự động, vui lòng không trả lời.</p>
                        </div>
                    </body>
                    </html>
                    """
                    .formatted(fullName, ticketList.toString());

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new EmailSendException("Failed to send booking cancelled email", e);
        }
    }

    @Override
    @Async("emailExecutor")
    public void sendBookingUpdatedEmail(String toEmail, String fullName, List<Tickets> tickets) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailConfig.getFromEmail());
            helper.setTo(toEmail);
            helper.setSubject("Thông báo cập nhật booking");

            StringBuilder ticketList = new StringBuilder();
            for (Tickets ticket : tickets) {
                ticketList.append("<li>Mã vé: ").append(ticket.getTicketCode())
                        .append(", Số ghế: ").append(ticket.getSeatNumber()).append("</li>");
            }

            String htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <title>Booking được cập nhật</title>
                    </head>
                    <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                        <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                            <h2 style="color: #4CAF50;">Booking của bạn đã được cập nhật</h2>
                            <p>Xin chào <strong>%s</strong>,</p>
                            <p>Thông tin booking của bạn đã được thay đổi. Danh sách vé mới:</p>
                            <ul>%s</ul>
                            <p>Nếu bạn có thắc mắc, vui lòng liên hệ hỗ trợ.</p>
                            <p style="font-size: 12px; color: #666;">Email này được gửi tự động, vui lòng không trả lời.</p>
                        </div>
                    </body>
                    </html>
                    """
                    .formatted(fullName, ticketList.toString());

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new EmailSendException("Failed to send booking updated email", e);
        }
    }

    @Override
    @Async("emailExecutor")
    public void sendComplaintStatusEmail(String toEmail, String fullName, String complaintStatus,
            String complaintContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailConfig.getFromEmail());
            helper.setTo(toEmail);
            helper.setSubject("Thông báo về khiếu nại");

            String htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <title>Trạng thái khiếu nại</title>
                    </head>
                    <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                        <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                            <h2 style="color: #2196F3;">Thông báo về khiếu nại</h2>
                            <p>Xin chào <strong>%s</strong>,</p>
                            <p>Trạng thái khiếu nại của bạn: <strong>%s</strong></p>
                            <p>Nội dung khiếu nại:</p>
                            <div style="background-color: #f5f5f5; padding: 10px; border-radius: 3px;">%s</div>
                            <p>Nếu bạn cần hỗ trợ thêm, vui lòng liên hệ với chúng tôi.</p>
                            <p style="font-size: 12px; color: #666;">Email này được gửi tự động, vui lòng không trả lời.</p>
                        </div>
                    </body>
                    </html>
                    """
                    .formatted(fullName, complaintStatus, complaintContent);

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new EmailSendException("Failed to send complaint status email", e);
        }
    }

    @Override
    @Async("emailExecutor")
    public void sendCustomerSupportEmail(String toEmail, String userName, String subject,
            String message, String caseNumber, String csRepName) {
        try {
            log.info("Preparing to send customer support email to: {}", toEmail);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(emailConfig.getFromEmail());
            helper.setTo(toEmail);
            helper.setSubject(subject);

            String htmlContent = buildCustomerSupportEmailContent(userName, message, caseNumber, csRepName);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

            log.info("Customer support email sent successfully to: {}", toEmail);

        } catch (MessagingException e) {
            log.error("Failed to send customer support email to {}: {}", toEmail, e.getMessage(), e);
            throw new EmailSendException("Failed to send customer support email", e);
        }
    }

    private String buildCustomerSupportEmailContent(String userName, String message,
            String caseNumber, String csRepName) {
        String caseReference = caseNumber != null && !caseNumber.isEmpty()
                ? "<p style=\"margin: 0 0 15px;\"><strong>Mã tham chiếu:</strong> " + caseNumber + "</p>"
                : "";

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Thông báo từ Busify</title>
                    <style>
                        @media only screen and (max-width: 600px) {
                            .container { padding: 15px !important; }
                            .header img { max-width: 150px !important; }
                            .content { padding: 15px !important; }
                            .footer { font-size: 11px !important; }
                        }
                    </style>
                </head>
                <body style="font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif; line-height: 1.6; color: #333333; background-color: #f4f4f9; margin: 0; padding: 20px;">
                    <div class="container" style="max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); overflow: hidden;">
                        <div class="header" style="background: linear-gradient(90deg, #4285F4, #34A853); padding: 20px; text-align: center;">
                            <h2 style="color: #ffffff; margin: 10px 0 0; font-size: 24px;">Busify Customer Support</h2>
                        </div>

                        <div class="content" style="padding: 25px;">
                            <p style="margin: 0 0 15px;">Kính gửi <strong>%s</strong>,</p>

                            %s

                            <div style="padding:15px 15px 15px 0px; margin: 20px 0; border-radius: 4px;">
                                %s
                            </div>

                            <p style="margin: 0 0 15px;">Nếu bạn có câu hỏi hoặc cần hỗ trợ thêm, vui lòng phản hồi email này hoặc liên hệ với chúng tôi qua số <a href="tel:+1234567890" style="color: #4285F4; text-decoration: none;">hotline</a>.</p>

                            <p style="margin: 0;">Trân trọng,<br>
                            Nhân viên Chăm sóc Khách hàng<br>
                            Busify</p>
                        </div>

                        <hr style="border: none; border-top: 1px solid #e2e8f0; margin: 20px 0;">

                        <div class="footer" style="font-size: 12px; color: #6b7280; text-align: center; padding: 15px;">
                            <p style="margin: 0;">© 2025 Busify. Tất cả các quyền được bảo lưu.</p>
                            <p style="margin: 5px 0 0;"><a href="https://busify.com" style="color: #4285F4; text-decoration: none;">busify.com</a> | <a href="mailto:support@busify.com" style="color: #4285F4; text-decoration: none;">support@busify.com</a></p>
                        </div>
                    </div>
                </body>
                </html>
                """
                .formatted(userName, caseReference, message == null ? "" : message.replace("\n", "<br>"), csRepName);
    }

    @Override
    @Async("emailExecutor")
    public void sendCustomerSupportEmailByTrip(String toEmail, String userName, String subject,
            String message, String csRepName, String route, String time, String busCompany) {
        try {
            log.info("Preparing to send customer support email by trip to: {}", toEmail);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(emailConfig.getFromEmail());
            helper.setTo(toEmail);
            helper.setSubject(subject);

            String htmlContent = buildCustomerSupportEmailContentByTrip(userName, message, csRepName, route, time,
                    busCompany);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

            log.info("Customer support email by trip sent successfully to: {}", toEmail);

        } catch (MessagingException e) {
            log.error("Failed to send customer support email by trip to {}: {}", toEmail, e.getMessage(), e);
            throw new EmailSendException("Failed to send customer support email by trip", e);
        }
    }

    @Override
    @Async("emailExecutor")
    public void sendBulkCustomerSupportEmailByTrip(List<String> toEmails, String subject, String message,
            String csRepName, String route, String time, String busCompany) {
        log.info("Starting bulk email send to {} recipients", toEmails.size());
        int successCount = 0;
        int failureCount = 0;

        for (String toEmail : toEmails) {
            try {
                // Reuse existing method for individual send
                sendCustomerSupportEmailByTrip(toEmail, "Khách hàng quý trọng", subject, message, csRepName, route,
                        time, busCompany);
                successCount++;
            } catch (Exception e) {
                log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
                failureCount++;
            }
        }

        log.info("Bulk email completed: {} success, {} failures", successCount, failureCount);
    }

    private String buildCustomerSupportEmailContentByTrip(String userName, String message, String csRepName,
            String route, String time, String busCompany) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Thông báo từ Busify</title>
                    <style>
                        @media only screen and (max-width: 600px) {
                            .container { padding: 15px !important; }
                            .header img { max-width: 150px !important; }
                            .content { padding: 15px !important; }
                            .footer { font-size: 11px !important; }
                        }
                    </style>
                </head>
                <body style="font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif; line-height: 1.6; color: #333333; background-color: #f4f4f9; margin: 0; padding: 20px;">
                    <div class="container" style="max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); overflow: hidden;">
                        <div class="header" style="background: linear-gradient(90deg, #4285F4, #34A853); padding: 20px; text-align: center;">
                            <h2 style="color: #ffffff; margin: 10px 0 0; font-size: 24px;">Busify Customer Support</h2>
                        </div>

                        <div class="content" style="padding: 25px;">
                            <p style="margin: 0 0 15px;">Kính gửi <strong>%s</strong>,</p>

                            <div style="padding:15px 15px 15px 0px; margin: 20px 0; border-radius: 4px;">
                                <table class="info-table" style="width: 100%%; border-collapse: collapse; margin: 0 0 10px;">
                                    <tr>
                                        <td style="padding: 8px; font-weight: bold; border-bottom: 1px solid #e2e8f0;">Tuyến đường:</td>
                                        <td style="padding: 8px; border-bottom: 1px solid #e2e8f0;">%s</td>
                                    </tr>
                                    <tr>
                                        <td style="padding: 8px; font-weight: bold; border-bottom: 1px solid #e2e8f0;">Thời gian:</td>
                                        <td style="padding: 8px; border-bottom: 1px solid #e2e8f0;">%s</td>
                                    </tr>
                                    <tr>
                                        <td style="padding: 8px; font-weight: bold; border-bottom: 1px solid #e2e8f0;">Nhà xe:</td>
                                        <td style="padding: 8px; border-bottom: 1px solid #e2e8f0;">%s</td>
                                    </tr>
                                </table>
                                %s
                            </div>

                            <p style="margin: 0 0 15px;">Nếu bạn có câu hỏi hoặc cần hỗ trợ thêm, vui lòng phản hồi email này hoặc liên hệ với chúng tôi qua số <a href="tel:+1234567890" style="color: #4285F4; text-decoration: none;">hotline</a>.</p>

                            <p style="margin: 0;">Trân trọng,<br>
                            Nhân viên Chăm sóc Khách hàng<br>
                            Busify</p>
                        </div>

                        <hr style="border: none; border-top: 1px solid #e2e8f0; margin: 20px 0;">

                        <div class="footer" style="font-size: 12px; color: #6b7280; text-align: center; padding: 15px;">
                            <p style="margin: 0;">© 2025 Busify. Tất cả các quyền được bảo lưu.</p>
                            <p style="margin: 5px 0 0;"><a href="https://busify.com" style="color: #4285F4; text-decoration: none;">busify.com</a> | <a href="mailto:support@busify.com" style="color: #4285F4; text-decoration: none;">support@busify.com</a></p>
                        </div>
                    </div>
                </body>
                </html>
                """
                .formatted(
                        userName,
                        route,
                        time,
                        busCompany,
                        message == null ? "" : message.replace("\n", "<br>"));
    }

    @Override
    @Async("emailExecutor")
    public void sendCustomerSupportEmailToBusOperator(String toEmail, String userName, String subject, String message,
            String csRepName) {
        try {
            log.info("Preparing to send customer support email to bus operator: {}", toEmail);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(emailConfig.getFromEmail());
            helper.setTo(toEmail);
            helper.setSubject(subject);

            String htmlContent = buildCustomerSupportEmailToBusOperator(userName, message, csRepName);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

            log.info("Customer support email to bus operator sent successfully to: {}", toEmail);

        } catch (MessagingException e) {
            log.error("Failed to send customer support email to bus operator {}: {}", toEmail, e.getMessage(), e);
            throw new EmailSendException("Failed to send customer support email to bus operator", e);
        }
    }

    private String buildCustomerSupportEmailToBusOperator(String userName, String message, String csRepName) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Thông báo từ Busify</title>
                    <style>
                        @media only screen and (max-width: 600px) {
                            .container { padding: 15px !important; }
                            .header img { max-width: 150px !important; }
                            .content { padding: 15px !important; }
                            .footer { font-size: 11px !important; }
                        }
                    </style>
                </head>
                <body style="font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif; line-height: 1.6; color: #333333; background-color: #f4f4f9; margin: 0; padding: 20px;">
                    <div class="container" style="max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); overflow: hidden;">
                        <div class="header" style="background: linear-gradient(90deg, #4285F4, #34A853); padding: 20px; text-align: center;">
                            <h2 style="color: #ffffff; margin: 10px 0 0; font-size: 24px;">Busify Customer Support</h2>
                        </div>

                        <div class="content" style="padding: 25px;">
                            <p style="margin: 0 0 15px;">Kính gửi <strong>%s</strong>,</p>

                            <div style="padding:15px 15px 15px 0px; margin: 20px 0; border-radius: 4px;">
                                %s
                            </div>

                            <p style="margin: 0 0 15px;">Nếu bạn có câu hỏi hoặc cần hỗ trợ thêm, vui lòng phản hồi email này hoặc liên hệ với chúng tôi qua số <a href="tel:+1234567890" style="color: #4285F4; text-decoration: none;">hotline</a>.</p>

                            <p style="margin: 0;">Trân trọng,<br>
                            Nhân viên Chăm sóc Khách hàng<br>
                            Busify</p>
                        </div>

                        <hr style="border: none; border-top: 1px solid #e2e8f0; margin: 20px 0;">

                        <div class="footer" style="font-size: 12px; color: #6b7280; text-align: center; padding: 15px;">
                            <p style="margin: 0;">© 2025 Busify. Tất cả các quyền được bảo lưu.</p>
                            <p style="margin: 5px 0 0;"><a href="https://busify.com" style="color: #4285F4; text-decoration: none;">busify.com</a> | <a href="mailto:support@busify.com" style="color: #4285F4; text-decoration: none;">support@busify.com</a></p>
                        </div>
                    </div>
                </body>
                </html>
                """
                .formatted(userName, message == null ? "" : message.replace("\n", "<br>"));
    }
}