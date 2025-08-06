package com.busify.project.payment.strategy;

import com.busify.project.payment.dto.request.PaymentRequestDTO;
import com.busify.project.payment.dto.response.PaymentResponseDTO;
import com.busify.project.payment.entity.Payment;

public interface PaymentStrategy {

    /**
     * Tạo payment URL cho payment method cụ thể
     * 
     * @param paymentEntity  Payment entity đã lưu trong DB
     * @param paymentRequest Request từ client
     * @return URL để redirect user đến payment gateway
     */
    String createPaymentUrl(Payment paymentEntity, PaymentRequestDTO paymentRequest);

    /**
     * Xử lý callback từ payment gateway
     * 
     * @param paymentEntity Payment entity từ DB
     * @param paymentId     Payment ID từ gateway
     * @param payerId       Payer ID từ gateway
     * @return Response sau khi execute payment
     */
    PaymentResponseDTO executePayment(Payment paymentEntity, String paymentId, String payerId);

    /**
     * Hủy payment
     * 
     * @param paymentEntity Payment entity từ DB
     * @param paymentId     Payment ID từ gateway
     * @return Response sau khi cancel payment
     */
    PaymentResponseDTO cancelPayment(Payment paymentEntity, String paymentId);

    /**
     * Kiểm tra payment method này có hỗ trợ không
     * 
     * @param paymentMethod Payment method cần kiểm tra
     * @return true nếu hỗ trợ
     */
    boolean supports(String paymentMethod);
}
