package com.busify.project.promotion.dto.campaign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionCampaignSummaryDTO {
    private Long campaignId;
    private String title;
    private String bannerUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
}
