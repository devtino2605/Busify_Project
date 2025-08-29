package com.busify.project.promotion.service;

import com.busify.project.promotion.dto.request.PromotionRequesDTO;
import com.busify.project.promotion.dto.response.PromotionResponseDTO;

import java.util.List;

public interface PromotionService {
    PromotionResponseDTO createPromotion(PromotionRequesDTO promotion);

    PromotionResponseDTO getPromotionById(Long id);
    PromotionResponseDTO getPromotionByCode(String code);
    List<PromotionResponseDTO> getAllPromotions();
    PromotionResponseDTO updatePromotion(Long id, PromotionRequesDTO promotion);
    void deletePromotion(Long id);
    void updateStatusExpiredPromotions();
}