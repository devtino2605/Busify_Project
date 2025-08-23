package com.busify.project.auth.service.impl;

import com.busify.project.ticket.entity.Tickets;
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

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
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
    @Async("emailExecutor")
    public void sendTicketEmail(String toEmail, String fullName, List<Tickets> tickets) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailConfig.getFromEmail());
            helper.setTo(toEmail);
            helper.setSubject("Xác nhận đặt vé của bạn");

            String htmlContent = buildTicketEmailContent(fullName, tickets);
            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new EmailSendException("Failed to send ticket email", e);
        }
    }

    private String buildTicketEmailContent(String fullName, List<Tickets> tickets) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
                .withZone(ZoneId.of("Asia/Ho_Chi_Minh")); // múi giờ VN
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
                        <p style="margin-top: 20px;">Chúc bạn có chuyến đi an toàn và vui vẻ! 🚌</p>
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
}