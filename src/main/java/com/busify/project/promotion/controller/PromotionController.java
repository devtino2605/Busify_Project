package com.busify.project.promotion.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.promotion.dto.request.PromotionRequesDTO;
import com.busify.project.promotion.dto.response.PromotionResponseDTO;
import com.busify.project.promotion.dto.response.UserPromotionResponseDTO;
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

    // Endpoints mới cho user promotion management
    @PostMapping("/claim/{userId}/{code}")
    public ApiResponse<UserPromotionResponseDTO> claimPromotion(@PathVariable Long userId, @PathVariable String code) {
            UserPromotionResponseDTO userPromotion = promotionService.claimPromotion(userId, code);
            return ApiResponse.<UserPromotionResponseDTO>builder()
                    .code(HttpStatus.OK.value())
                    .message("Promotion claimed successfully")
                    .result(userPromotion)
                    .build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<UserPromotionResponseDTO>> getUserPromotions(@PathVariable Long userId) {
        List<UserPromotionResponseDTO> userPromotions = promotionService.getUserPromotions(userId);
        return ApiResponse.<List<UserPromotionResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .result(userPromotions)
                .build();
    }

    @GetMapping("/user/{userId}/available")
    public ApiResponse<List<UserPromotionResponseDTO>> getUserAvailablePromotions(@PathVariable Long userId) {
        List<UserPromotionResponseDTO> availablePromotions = promotionService.getUserAvailablePromotions(userId);
        return ApiResponse.<List<UserPromotionResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .result(availablePromotions)
                .build();
    }

    @GetMapping("/check/{userId}/{code}")
    public ApiResponse<Boolean> canUsePromotion(@PathVariable Long userId, @PathVariable String code) {
        boolean canUse = promotionService.canUsePromotion(userId, code);
        return ApiResponse.<Boolean>builder()
                .code(HttpStatus.OK.value())
                .result(canUse)
                .build();
    }

    @PostMapping("/use/{userId}/{code}")
    public ApiResponse<Void> markPromotionAsUsed(@PathVariable Long userId, @PathVariable String code) {
        try {
            promotionService.markPromotionAsUsed(userId, code);
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("Promotion marked as used")
                    .build();
        } catch (RuntimeException e) {
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .build();
        }
    }
}