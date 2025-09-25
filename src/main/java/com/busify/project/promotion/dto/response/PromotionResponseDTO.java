package com.busify.project.promotion.dto.response;

import com.busify.project.promotion.enums.PromotionStatus;
import com.busify.project.promotion.enums.PromotionType;
import com.busify.project.promotion.enums.DiscountType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class PromotionResponseDTO {
    private Long id;
    private String code;
    private DiscountType discountType;
    private PromotionType promotionType;
    private BigDecimal discountValue;
    private BigDecimal minOrderValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer usageLimit;
    private PromotionStatus status;
    private Integer priority;
    private Long campaignId;
    private List<PromotionConditionResponseDTO> conditions;
}
