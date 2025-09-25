package com.busify.project.promotion.service;

import com.busify.project.promotion.dto.request.PromotionRequesDTO;
import com.busify.project.promotion.dto.request.PromotionFilterRequestDTO;
import com.busify.project.promotion.dto.response.PromotionResponseDTO;
import com.busify.project.promotion.dto.response.PromotionConditionResponseDTO;
import com.busify.project.promotion.dto.response.PromotionFilterResponseDTO;
import com.busify.project.promotion.dto.response.UserPromotionResponseDTO;
import com.busify.project.promotion.dto.response.UserPromotionConditionResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public interface PromotionService {
    PromotionResponseDTO createPromotion(PromotionRequesDTO promotion);

    PromotionResponseDTO getPromotionById(Long id);

    PromotionResponseDTO getPromotionByCode(String code);

    List<PromotionResponseDTO> getAllPromotions();

    // Filter promotions method
    PromotionFilterResponseDTO filterPromotions(PromotionFilterRequestDTO filter, int page, int size);

    PromotionResponseDTO updatePromotion(Long id, PromotionRequesDTO promotion);

    void deletePromotion(Long id);

    void updateStatusExpiredPromotions();

    // Methods mới cho việc quản lý promotion của user
    UserPromotionResponseDTO claimPromotion(String promotionCode, Long userId);

    // Methods for promotion conditions
    boolean areAllConditionsMet(Long userId, Long promotionId);

    void updateConditionProgress(Long conditionId, String progressData);

    List<PromotionConditionResponseDTO> getPromotionConditions(Long promotionId);

    List<UserPromotionResponseDTO> getUserPromotions(Long userId);

    List<UserPromotionResponseDTO> getUserAvailablePromotions(Long userId);

    List<UserPromotionResponseDTO> getUserUsedPromotions();

    boolean canUsePromotion(Long userId, String promotionCode);

    void markPromotionAsUsed(Long userId, String promotionCode);

    void removeMarkPromotionAsUsed(Long userId, String promotionCode);

    // Methods mới cho AUTO promotion validation
    List<PromotionResponseDTO> findActiveAutoPromotions(BigDecimal orderValue);

    PromotionResponseDTO findBestAutoPromotion(BigDecimal orderValue);

    PromotionResponseDTO validateAndApplyPromotion(Long userId, String promotionCode, BigDecimal orderValue);

    PromotionResponseDTO validateAndApplyPromotionById(Long userId, Long promotionId, BigDecimal orderValue);

    void createAndMarkAutoPromotionAsUsed(Long userId, Long promotionId);

    void removeAutoPromotionUsage(Long userId, Long promotionId);

    List<PromotionResponseDTO> getAllCurrentPromotions();

    void autoClaimEligiblePromotions(Long userId);

    List<UserPromotionConditionResponseDTO> getAllUserPromotionConditions();

    List<PromotionResponseDTO> getAutoPromotionsWithCompletedConditions();
}