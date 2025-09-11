package com.busify.project.refund.service.impl;

import com.busify.project.payment.entity.Payment;
import com.busify.project.payment.enums.PaymentStatus;
import com.busify.project.payment.repository.PaymentRepository;
import com.busify.project.refund.dto.request.RefundRequestDTO;
import com.busify.project.refund.dto.response.RefundResponseDTO;
import com.busify.project.refund.entity.Refund;
import com.busify.project.refund.enums.RefundStatus;
import com.busify.project.refund.exception.RefundNotFoundException;
import com.busify.project.refund.exception.RefundProcessingException;
import com.busify.project.refund.repository.RefundRepository;
import com.busify.project.refund.service.RefundService;
import com.busify.project.refund.strategy.RefundStrategy;
import com.busify.project.refund.strategy.RefundStrategyFactory;
import com.busify.project.refund.util.RefundPolicyUtil;
import com.busify.project.ticket.entity.Tickets;
import com.busify.project.user.entity.User;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.repository.UserRepository;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.auth.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefundServiceImpl implements RefundService {

    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final RefundStrategyFactory refundStrategyFactory;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;

    @Override
    @Transactional
    public RefundResponseDTO createRefund(RefundRequestDTO refundRequest) {
        try {
            log.info("Creating refund for payment ID: {}", refundRequest.getPaymentId());

            // Lấy payment
            Payment payment = paymentRepository.findById(refundRequest.getPaymentId())
                    .orElseThrow(() -> RefundProcessingException.paymentNotEligible());

            // Kiểm tra payment có thể refund không
            validatePaymentForRefund(payment);

            // Kiểm tra đã có refund nào chưa
            List<Refund> existingRefunds = refundRepository.findByPaymentPaymentId(payment.getPaymentId());
            if (!existingRefunds.isEmpty()) {
                throw RefundProcessingException.alreadyProcessed();
            }

            // Lấy current user
            User currentUser = getCurrentUser();

            // Tính toán refund amount và cancellation fee
            RefundPolicyUtil.RefundCalculation calculation = RefundPolicyUtil.calculateRefund(
                    payment.getAmount(),
                    payment.getBooking().getTrip().getDepartureTime());

            // Tạo refund entity
            Refund refund = new Refund();
            refund.setPayment(payment);
            refund.setRefundAmount(payment.getAmount());
            refund.setCancellationFee(calculation.getCancellationFee());
            refund.setNetRefundAmount(calculation.getNetRefundAmount());
            refund.setRefundReason(refundRequest.getRefundReason());
            refund.setStatus(RefundStatus.PENDING);
            refund.setRefundTransactionCode(generateRefundTransactionCode());
            refund.setRequestedBy(currentUser);
            refund.setNotes(refundRequest.getNotes());

            refund = refundRepository.save(refund);

            log.info("Refund created successfully with ID: {}", refund.getRefundId());

            processRefund(refund.getRefundId());

            log.info("Refund processed successfully with ID: {}", refund.getRefundId());

            return mapToDTO(refund);

        } catch (Exception e) {
            log.error("Error creating refund for payment ID: {}", refundRequest.getPaymentId(), e);
            throw RefundProcessingException.creationFailed(e);
        }
    }

    @Override
    @Transactional
    public RefundResponseDTO processRefund(Long refundId) {
        try {
            log.info("Processing refund ID: {}", refundId);

            Refund refund = refundRepository.findById(refundId)
                    .orElseThrow(() -> RefundNotFoundException.notFound(refundId));

            // Kiểm tra status
            if (refund.getStatus() != RefundStatus.PENDING) {
                throw RefundProcessingException.alreadyProcessed();
            }

            // Cập nhật status thành PROCESSING
            refund.setStatus(RefundStatus.PROCESSING);
            refund = refundRepository.save(refund);

            // Lấy strategy phù hợp
            RefundStrategy strategy = refundStrategyFactory.getStrategy(refund.getPayment().getPaymentMethod().name());

            // Xử lý refund thông qua strategy
            RefundResponseDTO result = strategy.processRefund(refund);

            // Kiểm tra nếu refund thành công thì gửi email
            if (RefundStatus.COMPLETED.name().equals(result.getStatus().name())) {
                // Lấy refund với tất cả dữ liệu cần thiết để tránh lazy loading
                Refund completedRefund = refundRepository.findByIdWithAllData(refundId)
                        .orElseThrow(() -> RefundNotFoundException.notFound(refundId));
                sendRefundSuccessEmailAsync(completedRefund);
            }

            log.info("Refund processed successfully for ID: {}", refundId);
            return result;

        } catch (Exception e) {
            log.error("Error processing refund ID: {}", refundId, e);
            throw RefundProcessingException.processingFailed(e);
        }
    }

    @Override
    public RefundResponseDTO getRefundById(Long refundId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> RefundNotFoundException.notFound(refundId));
        return mapToDTO(refund);
    }

    @Override
    public List<RefundResponseDTO> getRefundsByPaymentId(Long paymentId) {
        List<Refund> refunds = refundRepository.findByPaymentPaymentId(paymentId);
        return refunds.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RefundResponseDTO> getRefundsByCustomerId(Long customerId) {
        List<Refund> refunds = refundRepository.findByCustomerId(customerId);
        return refunds.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RefundResponseDTO checkRefundStatus(Long refundId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> RefundNotFoundException.notFound(refundId));

        // Lấy strategy và check status từ gateway
        RefundStrategy strategy = refundStrategyFactory.getStrategy(refund.getPayment().getPaymentMethod().name());
        return strategy.checkRefundStatus(refund);
    }

    @Override
    @Transactional
    public RefundResponseDTO cancelRefund(Long refundId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> RefundNotFoundException.notFound(refundId));

        // Chỉ có thể cancel khi status là PENDING
        if (refund.getStatus() != RefundStatus.PENDING) {
            throw RefundProcessingException.notAllowed();
        }

        refund.setStatus(RefundStatus.CANCELLED);
        refund = refundRepository.save(refund);

        log.info("Refund cancelled for ID: {}", refundId);
        return mapToDTO(refund);
    }

    @Override
    public List<RefundResponseDTO> getAllRefunds(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("requestedAt").descending());
        return refundRepository.findAll(pageable).getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private void validatePaymentForRefund(Payment payment) {
        // Kiểm tra payment status
        if (payment.getStatus() != PaymentStatus.completed) {
            throw RefundProcessingException.paymentNotEligible();
        }

        // Kiểm tra refund policy (thời gian)
        if (!RefundPolicyUtil.isRefundAllowed(payment.getBooking().getTrip().getDepartureTime())) {
            throw RefundProcessingException.policyViolation();
        }
    }

    private User getCurrentUser() {
        String email = jwtUtils.getCurrentUserLogin()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private String generateRefundTransactionCode() {
        // Format: REF_YYYYMMDD_TIMESTAMP_RANDOM
        // Ví dụ: REF_20250911_1694419200000_A8B9C2D1
        String datePrefix = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return String.format("REF_%s_%s_%s", datePrefix, timestamp, randomSuffix);
    }

    private RefundResponseDTO mapToDTO(Refund refund) {
        return RefundResponseDTO.builder()
                .refundId(refund.getRefundId())
                .paymentId(refund.getPayment().getPaymentId())
                .refundAmount(refund.getRefundAmount())
                .cancellationFee(refund.getCancellationFee())
                .netRefundAmount(refund.getNetRefundAmount())
                .refundReason(refund.getRefundReason())
                .status(refund.getStatus())
                .refundTransactionCode(refund.getRefundTransactionCode())
                .gatewayRefundId(refund.getGatewayRefundId())
                .requestedAt(refund.getRequestedAt())
                .processedAt(refund.getProcessedAt())
                .completedAt(refund.getCompletedAt())
                .notes(refund.getNotes())
                .build();
    }

    /**
     * Gửi email thông báo refund thành công
     * Fetch all needed data first to avoid lazy loading issues in async context
     */
    public void sendRefundSuccessEmailAsync(Refund refund) {
        try {
            // Fetch all needed data in current session before async call
            User customer = refund.getPayment().getBooking().getCustomer();
            String guestEmail = refund.getPayment().getBooking().getGuestEmail();
            String guestFullName = refund.getPayment().getBooking().getGuestFullName();

            // Force loading of tickets collection
            List<Tickets> tickets = refund.getPayment().getBooking().getTickets();

            String userEmail;
            String userFullName;

            if (customer != null) {
                // Trường hợp user đã đăng nhập
                userEmail = customer.getEmail();
                if (customer instanceof Profile) {
                    userFullName = ((Profile) customer).getFullName();
                } else {
                    userFullName = "Khách hàng";
                }
            } else {
                // Trường hợp guest booking
                userEmail = guestEmail;
                userFullName = guestFullName;
            }

            // Validate email
            if (userEmail == null || userEmail.trim().isEmpty()) {
                log.warn("Cannot send refund email: no email address found for refund ID {}", refund.getRefundId());
                return;
            }

            // Create a simple ticket data to avoid lazy loading in async context
            List<SimpleTicketData> simpleTickets = tickets.stream()
                    .map(ticket -> new SimpleTicketData(ticket.getTicketCode(), ticket.getSeatNumber()))
                    .collect(Collectors.toList());

            // Format số tiền hoàn
            String refundAmount = String.format("%,.0f", refund.getNetRefundAmount());
            String refundStatus = refund.getStatus().name();
            String refundReason = refund.getRefundReason();

            // Gửi email với dữ liệu đã fetch sẵn
            sendRefundEmailWithData(userEmail, userFullName, simpleTickets, refundAmount, refundStatus, refundReason,
                    refund.getRefundId());

        } catch (Exception e) {
            log.error("Failed to prepare refund success email for refund ID {}: {}",
                    refund.getRefundId(), e.getMessage(), e);
        }
    }

    /**
     * Internal method to send email with prepared data
     */
    private void sendRefundEmailWithData(String userEmail, String userFullName,
            List<SimpleTicketData> simpleTickets,
            String refundAmount, String refundStatus,
            String refundReason, Long refundId) {
        try {
            // Convert simple ticket data back to ticket objects for email service
            List<Tickets> emailTickets = simpleTickets.stream()
                    .map(st -> {
                        Tickets ticket = new Tickets();
                        ticket.setTicketCode(st.getTicketCode());
                        ticket.setSeatNumber(st.getSeatNumber());
                        return ticket;
                    })
                    .collect(Collectors.toList());

            // Gửi email
            emailService.sendBookingCancelledWithRefundEmail(
                    userEmail,
                    userFullName,
                    emailTickets,
                    refundAmount,
                    refundStatus,
                    refundReason);

            log.info("Refund success email sent to user {} for refund ID {}", userEmail, refundId);

        } catch (Exception e) {
            log.error("Failed to send refund success email to {} for refund ID {}: {}",
                    userEmail, refundId, e.getMessage(), e);
        }
    }

    /**
     * Simple data class to hold ticket information
     */
    private static class SimpleTicketData {
        private final String ticketCode;
        private final String seatNumber;

        public SimpleTicketData(String ticketCode, String seatNumber) {
            this.ticketCode = ticketCode;
            this.seatNumber = seatNumber;
        }

        public String getTicketCode() {
            return ticketCode;
        }

        public String getSeatNumber() {
            return seatNumber;
        }
    }
}
