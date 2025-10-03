package com.busify.project.promotion.dto.campaign;

import com.busify.project.promotion.enums.DiscountType;
import com.busify.project.promotion.enums.PromotionStatus;
import com.busify.project.promotion.enums.PromotionType;
import com.busify.project.promotion.dto.request.ValidDiscountValue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidDiscountValue
public class CampaignPromotionDTO {

    @NotNull(message = "Discount type is mandatory")
    private DiscountType discountType;

    @NotNull(message = "Promotion type is mandatory")
    private PromotionType promotionType;

    @NotNull(message = "Discount value is mandatory")
    @Positive(message = "Discount value must be positive")
    private BigDecimal discountValue;

    private BigDecimal minOrderValue;

    @Positive(message = "Usage limit must be positive")
    private Integer usageLimit;

    @NotNull(message = "Status is mandatory")
    private PromotionStatus status;

    @Positive(message = "Priority must be positive")
    private Integer priority;

}