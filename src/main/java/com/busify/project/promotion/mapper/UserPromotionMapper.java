package com.busify.project.promotion.mapper;

import com.busify.project.promotion.dto.response.UserPromotionResponseDTO;
import com.busify.project.promotion.entity.UserPromotion;

public class UserPromotionMapper {
    
    public static UserPromotionResponseDTO convertToDTO(UserPromotion userPromotion) {
        if (userPromotion == null) {
            return null;
        }

        return UserPromotionResponseDTO.builder()
                .userId(userPromotion.getUserId())
                .userEmail(userPromotion.getUser().getEmail())
                .promotionId(userPromotion.getPromotionId())
                .promotionCode(userPromotion.getPromotion().getCode())
                .discountType(userPromotion.getPromotion().getDiscountType())
                .discountValue(userPromotion.getPromotion().getDiscountValue())
                .promotionStartDate(userPromotion.getPromotion().getStartDate())
                .promotionEndDate(userPromotion.getPromotion().getEndDate())
                .claimedAt(userPromotion.getClaimedAt())
                .usedAt(userPromotion.getUsedAt())
                .isUsed(userPromotion.getIsUsed())
                .build();
    }
}