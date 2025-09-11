package com.busify.project.promotion.dto.request;

import com.busify.project.promotion.enums.PromotionStatus;
import com.busify.project.promotion.enums.PromotionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionFilterRequestDTO {
    private String search;
    private PromotionStatus status;
    private PromotionType type;
    private BigDecimal minDiscount;
    private BigDecimal maxDiscount;
    private LocalDate startDate;
    private LocalDate endDate;
}