package com.busify.project.promotion.mapper;

import com.busify.project.promotion.dto.response.UserPromotionConditionResponseDTO;
import com.busify.project.promotion.entity.Promotion;
import com.busify.project.promotion.entity.PromotionCondition;
import com.busify.project.promotion.entity.UserPromotionCondition;

public class UserPromotionConditionMapper {

    public static UserPromotionConditionResponseDTO toResponseDTO(UserPromotionCondition upc) {
        if (upc == null) {
            return null;
        }

        PromotionCondition condition = upc.getPromotionCondition();
        Promotion promotion = condition != null ? condition.getPromotion() : null;

        UserPromotionConditionResponseDTO dto = new UserPromotionConditionResponseDTO();
        dto.setUserPromotionConditionId(upc.getId());
        dto.setPromotionId(promotion != null ? promotion.getPromotionId() : null);
        dto.setPromotionCode(promotion != null ? promotion.getCode() : null);
        dto.setPromotionType(promotion != null && promotion.getPromotionType() != null
                ? promotion.getPromotionType().name()
                : null);
        if (condition != null) {
            dto.setConditionId(condition.getId());
            dto.setConditionType(condition.getConditionType());
            dto.setConditionValue(condition.getConditionValue());
            dto.setIsRequired(Boolean.TRUE.equals(condition.getIsRequired()));
        }
        dto.setIsCompleted(Boolean.TRUE.equals(upc.getIsCompleted()));
        dto.setCurrentProgress(upc.getProgressData());
        dto.setCompletedAt(upc.getCompletedAt() != null ? upc.getCompletedAt().toString() : null);
        return dto;
    }
}
