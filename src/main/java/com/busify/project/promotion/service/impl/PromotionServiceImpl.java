package com.busify.project.promotion.service.impl;

import com.busify.project.promotion.dto.request.PromotionRequesDTO;
import com.busify.project.promotion.dto.response.PromotionResponseDTO;
import com.busify.project.promotion.entity.Promotion;
import com.busify.project.promotion.mapper.PromotionMapper;
import com.busify.project.promotion.repository.PromotionRepository;
import com.busify.project.promotion.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;

    @Override
    public PromotionResponseDTO createPromotion(PromotionRequesDTO promotion) {
        String code = genneratePromotionCode();
        promotion.setCode(code);
        Promotion convertToPromotion = PromotionMapper.convertToEntity(promotion);
        Promotion savedPromotion = promotionRepository.save(convertToPromotion);
        return PromotionMapper.convertToDTO(savedPromotion);
    }

    private String genneratePromotionCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }

        return code.toString();
    }

    @Override
    public PromotionResponseDTO getPromotionById(Long id) {
        Optional<Promotion> promotion = promotionRepository.findById(id);
        return promotion.map(PromotionMapper::convertToDTO).orElse(null);
    }

    @Override
    public PromotionResponseDTO getPromotionByCode(String code) {

        Optional<Promotion> promotion = promotionRepository.findByCode(code);
        return promotion.map(PromotionMapper::convertToDTO).orElse(null);
    }

    @Override
    public List<PromotionResponseDTO> getAllPromotions() {

        List<Promotion> promotions = promotionRepository.findAll();
        return promotions.stream()
                .map(PromotionMapper::convertToDTO)
                .toList();
    }

    @Override
    public PromotionResponseDTO updatePromotion(Long id, PromotionRequesDTO promotion) {
        Optional<Promotion> existing = promotionRepository.findById(id);
        if (existing.isPresent()) {
            Promotion toUpdate = existing.get();
            toUpdate.setCode(promotion.getCode());
            toUpdate.setDiscountType(promotion.getDiscountType());
            toUpdate.setDiscountValue(promotion.getDiscountValue());
            toUpdate.setStartDate(promotion.getStartDate());
            toUpdate.setEndDate(promotion.getEndDate());
            toUpdate.setUsageLimit(promotion.getUsageLimit());
            toUpdate.setStatus(promotion.getStatus());

            Promotion updated = promotionRepository.save(toUpdate);
            return PromotionMapper.convertToDTO(updated);
        }
        return null;
    }

    @Override
    public void deletePromotion(Long id) {
        promotionRepository.deleteById(id);
    }
}
