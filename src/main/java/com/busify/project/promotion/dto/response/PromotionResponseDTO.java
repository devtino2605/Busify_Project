package com.busify.project.promotion.dto.response;

import com.busify.project.promotion.enums.PromotionStatus;
import com.busify.project.promotion.enums.DiscountType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PromotionResponseDTO {
    private Long id;
    private String code;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer usageLimit;
    private PromotionStatus status;
}
