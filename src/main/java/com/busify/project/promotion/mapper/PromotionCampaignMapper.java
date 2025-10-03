package com.busify.project.promotion.mapper;

import com.busify.project.common.service.CloudinaryService;
import com.busify.project.promotion.dto.campaign.PromotionCampaignCreateDTO;
import com.busify.project.promotion.dto.campaign.PromotionCampaignResponseDTO;
import com.busify.project.promotion.dto.campaign.PromotionCampaignSummaryDTO;
import com.busify.project.promotion.dto.campaign.PromotionCampaignUpdateDTO;
import com.busify.project.promotion.dto.response.PromotionResponseDTO;
import com.busify.project.promotion.entity.PromotionCampaign;
import com.busify.project.promotion.exception.PromotionCampaignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import com.busify.project.promotion.enums.DiscountType;

@Component
@RequiredArgsConstructor
public class PromotionCampaignMapper {

    private final CloudinaryService cloudinaryService;

    public PromotionCampaign toEntity(PromotionCampaignCreateDTO dto) {
        if (dto == null)
            return null;

        PromotionCampaign campaign = new PromotionCampaign();
        campaign.setTitle(dto.getTitle());
        campaign.setDescription(dto.getDescription());
        campaign.setDiscountValue(dto.getDiscountValue());
        campaign.setDiscountType(dto.getDiscountType() != null ? dto.getDiscountType() : DiscountType.FIXED_AMOUNT);
        campaign.setStartDate(dto.getStartDate());
        campaign.setEndDate(dto.getEndDate());
        campaign.setActive(dto.getActive());

        // Upload banner if provided
        if (dto.getBanner() != null && !dto.getBanner().isEmpty()) {
            try {
                String bannerUrl = cloudinaryService.uploadFile(dto.getBanner(), "campaigns/");
                campaign.setBannerUrl(bannerUrl);
            } catch (Exception e) {
                throw PromotionCampaignException.bannerUploadFailed(dto.getBanner().getOriginalFilename(), e);
            }
        }

        return campaign;
    }

    public void updateEntity(PromotionCampaign campaign, PromotionCampaignUpdateDTO dto) {
        if (campaign == null || dto == null)
            return;

        campaign.setTitle(dto.getTitle());
        campaign.setDescription(dto.getDescription());
        // Only update discountValue/type if provided in update DTO
        if (dto.getDiscountValue() != null) {
            campaign.setDiscountValue(dto.getDiscountValue());
        }
        if (dto.getDiscountType() != null) {
            campaign.setDiscountType(dto.getDiscountType());
        }
        campaign.setStartDate(dto.getStartDate());
        campaign.setEndDate(dto.getEndDate());
        if (dto.getActive() != null) {
            campaign.setActive(dto.getActive());
        }

        // Update banner if provided
        if (dto.getBanner() != null && !dto.getBanner().isEmpty()) {
            try {
                // Delete old banner if exists
                if (campaign.getBannerUrl() != null) {
                    String oldPublicId = cloudinaryService.extractPublicId(campaign.getBannerUrl());
                    cloudinaryService.deleteFile(oldPublicId);
                }

                // Upload new banner
                String bannerUrl = cloudinaryService.uploadFile(dto.getBanner(), "campaigns/");
                campaign.setBannerUrl(bannerUrl);
            } catch (Exception e) {
                throw PromotionCampaignException.bannerUploadFailed(dto.getBanner().getOriginalFilename(), e);
            }
        }
    }

    public PromotionCampaignResponseDTO toResponseDTO(PromotionCampaign campaign) {
        if (campaign == null)
            return null;

        PromotionCampaignResponseDTO dto = new PromotionCampaignResponseDTO();
        dto.setCampaignId(campaign.getCampaignId());
        dto.setTitle(campaign.getTitle());
        dto.setDescription(campaign.getDescription());
        dto.setBannerUrl(campaign.getBannerUrl());
        dto.setDiscountValue(campaign.getDiscountValue());
        dto.setDiscountType(campaign.getDiscountType() != null ? campaign.getDiscountType().name() : null);
        dto.setStartDate(campaign.getStartDate());
        dto.setEndDate(campaign.getEndDate());
        dto.setActive(campaign.getActive());
        dto.setDeleted(campaign.getDeleted());

        // Count and convert promotions in this campaign
        if (campaign.getPromotions() != null) {
            dto.setPromotionCount(campaign.getPromotions().size());

            // Convert promotions to DTOs
            List<PromotionResponseDTO> promotionDTOs = campaign.getPromotions().stream()
                    .map(PromotionMapper::convertToDTO)
                    .collect(java.util.stream.Collectors.toList());
            dto.setPromotions(promotionDTOs);
        } else {
            dto.setPromotionCount(0);
            dto.setPromotions(new ArrayList<>());
        }

        return dto;
    }

    public PromotionCampaignSummaryDTO toSummaryDTO(PromotionCampaign campaign) {
        if (campaign == null)
            return null;

        PromotionCampaignSummaryDTO dto = new PromotionCampaignSummaryDTO();
        dto.setCampaignId(campaign.getCampaignId());
        dto.setTitle(campaign.getTitle());
        dto.setBannerUrl(campaign.getBannerUrl());
        dto.setStartDate(campaign.getStartDate());
        dto.setEndDate(campaign.getEndDate());
        dto.setActive(campaign.getActive());

        return dto;
    }
}