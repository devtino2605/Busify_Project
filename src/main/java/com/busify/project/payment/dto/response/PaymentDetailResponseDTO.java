package com.busify.project.payment.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

import com.busify.project.booking.dto.response.BookingDetailResponseDTO;
import com.busify.project.payment.enums.PaymentMethod;
import com.busify.project.payment.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDetailResponseDTO {
    private Long paymentId;
    
    private BigDecimal amount;
    
    private String transactionCode;
    
    private PaymentMethod paymentMethod;
    
    
    private BookingDetailResponseDTO bookingDetails;
    
    
    private String customerName;
    
   
    private String customerEmail;
    
    private String customerPhone;
    
    private PaymentStatus status;
    
    private Instant paidAt;
}
