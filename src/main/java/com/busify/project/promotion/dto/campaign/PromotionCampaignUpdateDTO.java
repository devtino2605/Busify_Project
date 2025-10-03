package com.busify.project.promotion.dto.campaign;

import com.busify.project.promotion.enums.DiscountType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionCampaignUpdateDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    private MultipartFile banner;

    @Min(value = 0, message = "Discount value must be non-negative")
    private BigDecimal discountValue;

    private DiscountType discountType;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private Boolean active;

    // Add promotions to allow updating them with campaign
    @Valid
    private List<CampaignPromotionDTO> promotions;

    // List of existing promotion IDs to link to this campaign
    private List<Long> existingPromotionIds;
}