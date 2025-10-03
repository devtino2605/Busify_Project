package com.busify.project.promotion.mapper;

import com.busify.project.promotion.dto.request.PromotionRequesDTO;
import com.busify.project.promotion.dto.response.PromotionResponseDTO;
import com.busify.project.promotion.entity.Promotion;

public class PromotionMapper {
    public static PromotionResponseDTO convertToDTO(Promotion promotion) {
        if (promotion == null) {
            return null;
        }
        PromotionResponseDTO dto = new PromotionResponseDTO();
        dto.setId(promotion.getPromotionId());
        dto.setCode(promotion.getCode());
        dto.setDiscountType(promotion.getDiscountType());
        dto.setPromotionType(promotion.getPromotionType());
        dto.setDiscountValue(promotion.getDiscountValue());
        dto.setMinOrderValue(promotion.getMinOrderValue());
        dto.setStatus(promotion.getStatus());
        dto.setStartDate(promotion.getStartDate());
        dto.setEndDate(promotion.getEndDate());
        dto.setUsageLimit(promotion.getUsageLimit());
        dto.setPriority(promotion.getPriority());
        dto.setConditions(promotion.getConditions().stream()
                .map(PromotionConditionMapper::toResponseDTO)
                .toList());
        dto.setCampaignId(
                promotion.getCampaign() != null && promotion.getCampaign().getCampaignId() != null
                        ? promotion.getCampaign().getCampaignId()
                        : null);
        return dto;
    }

    public static Promotion convertToEntity(PromotionRequesDTO dto) {
        if (dto == null) {
            return null;
        }
        Promotion promotion = new Promotion();
        promotion.setCode(dto.getCode());
        promotion.setPromotionType(dto.getPromotionType());
        promotion.setDiscountType(dto.getDiscountType());
        promotion.setDiscountValue(dto.getDiscountValue());
        promotion.setMinOrderValue(dto.getMinOrderValue());
        promotion.setStatus(dto.getStatus());
        promotion.setStartDate(dto.getStartDate());
        promotion.setEndDate(dto.getEndDate());
        promotion.setUsageLimit(dto.getUsageLimit());
        promotion.setPriority(dto.getPriority());
        return promotion;
    }
}
