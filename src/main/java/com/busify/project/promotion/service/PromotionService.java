package com.busify.project.promotion.service;

import com.busify.project.promotion.dto.request.PromotionRequesDTO;
import com.busify.project.promotion.dto.response.PromotionResponseDTO;
import com.busify.project.promotion.dto.response.UserPromotionResponseDTO;

import java.util.List;

public interface PromotionService {
    PromotionResponseDTO createPromotion(PromotionRequesDTO promotion);

    PromotionResponseDTO getPromotionById(Long id);
    PromotionResponseDTO getPromotionByCode(String code);
    List<PromotionResponseDTO> getAllPromotions();
    PromotionResponseDTO updatePromotion(Long id, PromotionRequesDTO promotion);
    void deletePromotion(Long id);
    void updateStatusExpiredPromotions();

    // Methods mới cho việc quản lý promotion của user
    UserPromotionResponseDTO claimPromotion(Long userId, String promotionCode);
    List<UserPromotionResponseDTO> getUserPromotions(Long userId);
    List<UserPromotionResponseDTO> getUserAvailablePromotions(Long userId);
    boolean canUsePromotion(Long userId, String promotionCode);
    void markPromotionAsUsed(Long userId, String promotionCode);
    void removeMarkPromotionAsUsed(Long userId, String promotionCode);
}