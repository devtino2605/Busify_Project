package com.busify.project.promotion.dto.request;

import com.busify.project.promotion.enums.DiscountType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class ValidDiscountValueValidator implements ConstraintValidator<ValidDiscountValue, PromotionRequesDTO> {
    @Override
    public boolean isValid(PromotionRequesDTO dto, ConstraintValidatorContext context) {
        if (dto == null || dto.getDiscountType() == null || dto.getDiscountValue() == null) {
            return true; // Let @NotNull handle nulls
        }
        BigDecimal value = dto.getDiscountValue();
        DiscountType type = dto.getDiscountType();
        if (type == DiscountType.PERCENTAGE) {
            // Allow decimal percentages, e.g., 12.5%
            return value.compareTo(BigDecimal.ZERO) > 0 && value.compareTo(BigDecimal.valueOf(100)) <= 0;
        } else if (type == DiscountType.FIXED_AMOUNT) {
            return value.compareTo(BigDecimal.ZERO) > 0;
        }
        return true;
    }
}
