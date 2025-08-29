package com.busify.project.promotion.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.promotion.dto.request.PromotionRequesDTO;
import com.busify.project.promotion.dto.response.PromotionResponseDTO;
import com.busify.project.promotion.service.PromotionService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @PostMapping
    public ApiResponse<PromotionResponseDTO> createPromotion(@RequestBody PromotionRequesDTO promotion) {
        PromotionResponseDTO created = promotionService.createPromotion(promotion);
        return ApiResponse.<PromotionResponseDTO>builder()
                .code(HttpStatus.CREATED.value())
                .result(created)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<PromotionResponseDTO> getPromotionById(@PathVariable Long id) {
        PromotionResponseDTO promotion = promotionService.getPromotionById(id);
        return ApiResponse.<PromotionResponseDTO>builder()
                .code(promotion != null ? HttpStatus.OK.value() : HttpStatus.NOT_FOUND.value())
                .result(promotion)
                .build();
    }

    @GetMapping("/code/{code}")
    public ApiResponse<PromotionResponseDTO> getPromotionByCode(@PathVariable String code) {
        PromotionResponseDTO promotion = promotionService.getPromotionByCode(code);
        return ApiResponse.<PromotionResponseDTO>builder()
                .code(promotion != null ? HttpStatus.OK.value() : HttpStatus.NOT_FOUND.value())
                .result(promotion)
                .build();
    }

    @GetMapping
    public ApiResponse<List<PromotionResponseDTO>> getAllPromotions() {
        List<PromotionResponseDTO> promotions = promotionService.getAllPromotions();
        return ApiResponse.<List<PromotionResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .result(promotions)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<PromotionResponseDTO> updatePromotion(@PathVariable Long id,
            @RequestBody PromotionRequesDTO promotion) {
        PromotionResponseDTO updated = promotionService.updatePromotion(id, promotion);
        return ApiResponse.<PromotionResponseDTO>builder()
                .code(updated != null ? HttpStatus.OK.value() : HttpStatus.NOT_FOUND.value())
                .result(updated)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .build();
    }
}
