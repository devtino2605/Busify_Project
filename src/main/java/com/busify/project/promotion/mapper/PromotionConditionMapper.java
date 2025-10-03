package com.busify.project.promotion.mapper;

import com.busify.project.promotion.dto.response.PromotionConditionResponseDTO;
import com.busify.project.promotion.entity.PromotionCondition;

public class PromotionConditionMapper {

    public static PromotionConditionResponseDTO toResponseDTO(PromotionCondition entity) {
        if (entity == null) {
            return null;
        }

        PromotionConditionResponseDTO dto = new PromotionConditionResponseDTO();
        dto.setConditionId(entity.getId());
        dto.setConditionType(entity.getConditionType());
        dto.setConditionValue(entity.getConditionValue());
        dto.setIsRequired(entity.getIsRequired());

        return dto;
    }
}
