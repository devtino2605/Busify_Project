package com.busify.project.promotion.dto.request;

import com.busify.project.promotion.dto.campaign.CampaignPromotionDTO;
import com.busify.project.promotion.enums.DiscountType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class ValidDiscountValueValidator implements ConstraintValidator<ValidDiscountValue, Object> {
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj == null) {
            return true; // Let @NotNull handle nulls
        }

        DiscountType discountType = null;
        BigDecimal discountValue = null;

        // Handle PromotionRequesDTO
        if (obj instanceof PromotionRequesDTO) {
            PromotionRequesDTO dto =
                (PromotionRequesDTO) obj;
            discountType = dto.getDiscountType();
            discountValue = dto.getDiscountValue();
        }
        // Handle CampaignPromotionDTO
        else if (obj instanceof CampaignPromotionDTO) {
            CampaignPromotionDTO dto =
                (CampaignPromotionDTO) obj;
            discountType = dto.getDiscountType();
            discountValue = dto.getDiscountValue();
        }

        if (discountType == null || discountValue == null) {
            return true; // Let @NotNull handle nulls
        }

        if (discountType == DiscountType.PERCENTAGE) {
            // Allow decimal percentages, e.g., 12.5%
            return discountValue.compareTo(BigDecimal.ZERO) > 0 && discountValue.compareTo(BigDecimal.valueOf(100)) <= 0;
        } else if (discountType == DiscountType.FIXED_AMOUNT) {
            return discountValue.compareTo(BigDecimal.ZERO) > 0;
        }
        return true;
    }
}