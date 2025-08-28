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
        dto.setDiscountValue(promotion.getDiscountValue());
        dto.setStatus(promotion.getStatus());
        dto.setStartDate(promotion.getStartDate());
        dto.setEndDate(promotion.getEndDate());
        dto.setUsageLimit(promotion.getUsageLimit());
        return dto;
    }

    public static Promotion convertToEntity(PromotionRequesDTO dto) {
        if (dto == null) {
            return null;
        }
        Promotion promotion = new Promotion();
        promotion.setCode(dto.getCode());
        promotion.setDiscountType(dto.getDiscountType());
        promotion.setDiscountValue(dto.getDiscountValue());
        promotion.setStatus(dto.getStatus());
        promotion.setStartDate(dto.getStartDate());
        promotion.setEndDate(dto.getEndDate());
        promotion.setUsageLimit(dto.getUsageLimit());
        return promotion;
    }
}
