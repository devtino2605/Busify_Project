package com.busify.project.refund.service;

import com.busify.project.refund.dto.request.RefundRequestDTO;
import com.busify.project.refund.dto.response.RefundResponseDTO;

import java.util.List;

public interface RefundService {

    /**
     * Tạo yêu cầu refund mới
     */
    RefundResponseDTO createRefund(RefundRequestDTO refundRequest);

    /**
     * Xử lý refund thông qua payment gateway
     */
    RefundResponseDTO processRefund(Long refundId);

    /**
     * Lấy thông tin refund theo ID
     */
    RefundResponseDTO getRefundById(Long refundId);

    /**
     * Lấy danh sách refund theo payment ID
     */
    List<RefundResponseDTO> getRefundsByPaymentId(Long paymentId);

    /**
     * Lấy danh sách refund theo customer ID
     */
    List<RefundResponseDTO> getRefundsByCustomerId(Long customerId);

    /**
     * Kiểm tra trạng thái refund từ payment gateway
     */
    RefundResponseDTO checkRefundStatus(Long refundId);

    /**
     * Hủy yêu cầu refund
     */
    RefundResponseDTO cancelRefund(Long refundId);

    /**
     * Lấy tất cả refund với phân trang
     */
    List<RefundResponseDTO> getAllRefunds(int page, int size);
}
