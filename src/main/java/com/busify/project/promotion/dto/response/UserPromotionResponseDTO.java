package com.busify.project.promotion.dto.response;

import com.busify.project.promotion.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPromotionResponseDTO {
    private Long userId;
    private String userEmail;
    private Long promotionId;
    private String promotionCode;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private LocalDate promotionStartDate;
    private LocalDate promotionEndDate;
    private LocalDateTime claimedAt;
    private LocalDateTime usedAt;
    private Boolean isUsed;
}