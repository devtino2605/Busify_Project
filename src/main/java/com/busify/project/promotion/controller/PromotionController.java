package com.busify.project.promotion.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.promotion.dto.request.PromotionRequesDTO;
import com.busify.project.promotion.dto.request.PromotionFilterRequestDTO;
import com.busify.project.promotion.dto.response.PromotionResponseDTO;
import com.busify.project.promotion.dto.response.PromotionFilterResponseDTO;
import com.busify.project.promotion.dto.response.UserPromotionResponseDTO;
import com.busify.project.promotion.enums.PromotionStatus;
import com.busify.project.promotion.enums.PromotionType;
import com.busify.project.promotion.service.PromotionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
@Tag(name = "Promotions", description = "Promotion Management API")
public class PromotionController {
    private final PromotionService promotionService;

    @Operation(summary = "Create a new promotion")
    @PostMapping
    public ApiResponse<PromotionResponseDTO> createPromotion(@Valid @RequestBody PromotionRequesDTO promotion) {
        PromotionResponseDTO created = promotionService.createPromotion(promotion);
        return ApiResponse.<PromotionResponseDTO>builder()
                .code(HttpStatus.CREATED.value())
                .result(created)
                .build();
    }

    @Operation(summary = "Get promotion by ID")
    @GetMapping("/{id}")
    public ApiResponse<PromotionResponseDTO> getPromotionById(@PathVariable Long id) {
        PromotionResponseDTO promotion = promotionService.getPromotionById(id);
        return ApiResponse.<PromotionResponseDTO>builder()
                .code(promotion != null ? HttpStatus.OK.value() : HttpStatus.NOT_FOUND.value())
                .result(promotion)
                .build();
    }

    @Operation(summary = "Get promotion by code")
    @GetMapping("/code/{code}")
    public ApiResponse<PromotionResponseDTO> getPromotionByCode(@PathVariable String code) {
        PromotionResponseDTO promotion = promotionService.getPromotionByCode(code);
        return ApiResponse.<PromotionResponseDTO>builder()
                .code(promotion != null ? HttpStatus.OK.value() : HttpStatus.NOT_FOUND.value())
                .result(promotion)
                .build();
    }

    @Operation(summary = "Get all promotions")
    @GetMapping
    public ApiResponse<List<PromotionResponseDTO>> getAllPromotions() {
        List<PromotionResponseDTO> promotions = promotionService.getAllPromotions();
        return ApiResponse.<List<PromotionResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .result(promotions)
                .build();
    }

    @Operation(summary = "Filter promotions with various criteria", description = "Filter promotions by search term, status, type, discount range, and date range")
    @GetMapping("/filter")
    public ApiResponse<PromotionFilterResponseDTO> filterPromotions(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) PromotionStatus status,
            @RequestParam(required = false) PromotionType type,
            @RequestParam(required = false) BigDecimal minDiscount,
            @RequestParam(required = false) BigDecimal maxDiscount,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            // Tạo filter DTO từ query parameters
            PromotionFilterRequestDTO filter = new PromotionFilterRequestDTO(
                    search, status, type, minDiscount, maxDiscount, startDate, endDate);

            PromotionFilterResponseDTO filteredPromotions = promotionService.filterPromotions(filter, page, size);
            return ApiResponse.success("Lọc promotion thành công", filteredPromotions);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Đã xảy ra lỗi khi lọc promotion: " + e.getMessage());
        }
    }

    @Operation(summary = "Update promotion by ID")
    @PutMapping("/{id}")
    public ApiResponse<PromotionResponseDTO> updatePromotion(@PathVariable Long id,
            @Valid @RequestBody PromotionRequesDTO promotion) {
        PromotionResponseDTO updated = promotionService.updatePromotion(id, promotion);
        return ApiResponse.<PromotionResponseDTO>builder()
                .code(updated != null ? HttpStatus.OK.value() : HttpStatus.NOT_FOUND.value())
                .result(updated)
                .build();
    }

    @Operation(summary = "Delete promotion by ID")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Promotion deleted successfully")
                .build();
    }

    @Operation(summary = "Claim a promotion using code")
    @PostMapping("/claim/{code}")
    public ApiResponse<UserPromotionResponseDTO> claimPromotion(@PathVariable String code) {
        UserPromotionResponseDTO userPromotion = promotionService.claimPromotion(code);
        return ApiResponse.<UserPromotionResponseDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Promotion claimed successfully")
                .result(userPromotion)
                .build();
    }

    @Operation(summary = "Get user's promotions by user ID")
    @GetMapping("/user/{userId}")
    public ApiResponse<List<UserPromotionResponseDTO>> getUserPromotions(@PathVariable Long userId) {
        List<UserPromotionResponseDTO> userPromotions = promotionService.getUserPromotions(userId);
        return ApiResponse.<List<UserPromotionResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .result(userPromotions)
                .build();
    }

    @Operation(summary = "Get user's available promotions")
    @GetMapping("/user/{userId}/available")
    public ApiResponse<List<UserPromotionResponseDTO>> getUserAvailablePromotions(@PathVariable Long userId) {
        List<UserPromotionResponseDTO> availablePromotions = promotionService.getUserAvailablePromotions(userId);
        return ApiResponse.<List<UserPromotionResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .result(availablePromotions)
                .build();
    }

    @Operation(summary = "Get current user's used promotions")
    @GetMapping("/user/used")
    public ApiResponse<List<UserPromotionResponseDTO>> getUserUsedPromotions() {
        List<UserPromotionResponseDTO> usedPromotions = promotionService.getUserUsedPromotions();
        return ApiResponse.<List<UserPromotionResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .result(usedPromotions)
                .build();
    }

    @Operation(summary = "Mark promotion as used")
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