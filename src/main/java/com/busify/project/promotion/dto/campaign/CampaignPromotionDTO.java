package com.busify.project.promotion.dto.campaign;

import com.busify.project.promotion.enums.DiscountType;
import com.busify.project.promotion.enums.PromotionStatus;
import com.busify.project.promotion.enums.PromotionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignPromotionDTO {

    private DiscountType discountType;
    private PromotionType promotionType;
    private BigDecimal discountValue;
    private BigDecimal minOrderValue;
    private Integer usageLimit;
    private PromotionStatus status;
    private Integer priority;

}
