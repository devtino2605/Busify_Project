package com.busify.project.promotion.dto.campaign;

import com.busify.project.promotion.dto.response.PromotionResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionCampaignResponseDTO {
    private Long campaignId;
    private String title;
    private String description;
    private String bannerUrl;
    private BigDecimal discountValue;
    private String discountType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
    private Integer promotionCount;
    private Boolean deleted;

    // Add list of promotions in this campaign
    private List<PromotionResponseDTO> promotions;
}