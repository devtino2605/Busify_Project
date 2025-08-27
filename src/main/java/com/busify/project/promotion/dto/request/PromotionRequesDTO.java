package com.busify.project.promotion.dto.request;

import com.busify.project.promotion.enums.PromotionStatus;
import com.busify.project.promotion.enums.DiscountType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Future;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ValidDiscountValue
public class PromotionRequesDTO {
    private String code;

    @NotNull(message = "Discount type is mandatory")
    private DiscountType discountType;

    @NotNull(message = "Discount value is mandatory")
    private BigDecimal discountValue;
    
    @NotNull(message = "Start date is mandatory")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is mandatory")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @NotNull(message = "Usage limit is mandatory")
    @Min(value = 1, message = "Usage limit must be at least 1")
    private Integer usageLimit;

    @NotNull(message = "Status is mandatory")
    private PromotionStatus status;
}