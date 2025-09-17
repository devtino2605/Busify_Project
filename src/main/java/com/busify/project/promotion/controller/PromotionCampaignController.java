package com.busify.project.promotion.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.promotion.dto.campaign.CampaignPromotionDTO;
import com.busify.project.promotion.dto.campaign.PromotionCampaignCreateDTO;
import com.busify.project.promotion.dto.campaign.PromotionCampaignFilterResponseDTO;
import com.busify.project.promotion.dto.campaign.PromotionCampaignResponseDTO;
import com.busify.project.promotion.dto.campaign.PromotionCampaignSummaryDTO;
import com.busify.project.promotion.dto.campaign.PromotionCampaignUpdateDTO;
import com.busify.project.promotion.enums.DiscountType;
import com.busify.project.promotion.enums.PromotionStatus;
import com.busify.project.promotion.enums.PromotionType;
import com.busify.project.promotion.service.PromotionCampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/promotion-campaigns")
@RequiredArgsConstructor
@Tag(name = "Promotion Campaign Management", description = "API for managing promotion campaigns")
public class PromotionCampaignController {

        private final PromotionCampaignService campaignService;

        @Operation(summary = "Create new promotion campaign", description = "Create a new promotion campaign with banner upload and optional promotions")
        @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        @PreAuthorize("hasAuthority('MANAGE_PROMOTIONS') or hasRole('ADMIN')")
        public ApiResponse<PromotionCampaignResponseDTO> createCampaign(
                        @RequestParam("title") String title,
                        @RequestParam(value = "description", required = false) String description,
                        @RequestParam(value = "banner", required = false) MultipartFile banner,
                        @RequestParam("startDate") String startDate,
                        @RequestParam("endDate") String endDate,
                        @RequestParam(value = "active", defaultValue = "true") Boolean active,
                        HttpServletRequest request) {

                try {
                        // Create DTO manually from request parameters
                        PromotionCampaignCreateDTO createDTO = new PromotionCampaignCreateDTO();
                        createDTO.setTitle(title);
                        createDTO.setDescription(description);
                        createDTO.setBanner(banner);
                        createDTO.setStartDate(LocalDate.parse(startDate));
                        createDTO.setEndDate(LocalDate.parse(endDate));
                        createDTO.setActive(active);

                        // Parse promotions from request parameters
                        List<CampaignPromotionDTO> promotions = parsePromotionsFromRequest(request);
                        createDTO.setPromotions(promotions);

                        // Parse existing promotion IDs
                        List<Long> existingPromotionIds = parseExistingPromotionIds(request);
                        System.out.println("Existing Promotion IDs: " + existingPromotionIds);
                        createDTO.setExistingPromotionIds(existingPromotionIds);

                        PromotionCampaignResponseDTO response = campaignService.createCampaign(createDTO);
                        return ApiResponse.<PromotionCampaignResponseDTO>builder()
                                        .code(HttpStatus.CREATED.value())
                                        .message("Promotion campaign created successfully")
                                        .result(response)
                                        .build();
                } catch (IllegalArgumentException e) {
                        return ApiResponse.<PromotionCampaignResponseDTO>builder()
                                        .code(HttpStatus.BAD_REQUEST.value())
                                        .message("Validation error: " + e.getMessage())
                                        .build();
                } catch (Exception e) {
                        return ApiResponse.<PromotionCampaignResponseDTO>builder()
                                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                        .message("Error creating campaign: " + e.getMessage())
                                        .build();
                }
        }

        @Operation(summary = "Get campaign by ID", description = "Retrieve a specific promotion campaign by its ID")
        @GetMapping("/{campaignId}")
        public ApiResponse<PromotionCampaignResponseDTO> getCampaignById(
                        @Parameter(description = "Campaign ID") @PathVariable Long campaignId) {
                PromotionCampaignResponseDTO response = campaignService.getCampaignById(campaignId);
                return ApiResponse.<PromotionCampaignResponseDTO>builder()
                                .code(HttpStatus.OK.value())
                                .message("Promotion campaign retrieved successfully")
                                .result(response)
                                .build();
        }

        @Operation(summary = "Get all campaigns", description = "Retrieve all promotion campaigns with pagination")
        @GetMapping
        public ApiResponse<PromotionCampaignFilterResponseDTO> getAllCampaigns(
                        @Parameter(description = "Page number (1-based)") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
                        @Parameter(description = "Sort by field") @RequestParam(defaultValue = "campaignId") String sortBy,
                        @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

                PromotionCampaignFilterResponseDTO response = campaignService.getAllCampaignsSimple(page, size, sortBy,
                                sortDir);
                return ApiResponse.<PromotionCampaignFilterResponseDTO>builder()
                                .code(HttpStatus.OK.value())
                                .message("Promotion campaigns retrieved successfully")
                                .result(response)
                                .build();
        }

        @Operation(summary = "Get active campaigns", description = "Retrieve all active promotion campaigns")
        @GetMapping("/active")
        public ApiResponse<List<PromotionCampaignSummaryDTO>> getActiveCampaigns() {
                List<PromotionCampaignSummaryDTO> response = campaignService.getActiveCampaigns();
                return ApiResponse.<List<PromotionCampaignSummaryDTO>>builder()
                                .code(HttpStatus.OK.value())
                                .message("Active promotion campaigns retrieved successfully")
                                .result(response)
                                .build();
        }

        @Operation(summary = "Get current campaigns", description = "Retrieve campaigns currently running (within date range)")
        @GetMapping("/current")
        public ApiResponse<List<PromotionCampaignSummaryDTO>> getCurrentCampaigns() {
                List<PromotionCampaignSummaryDTO> response = campaignService.getCurrentCampaigns();
                return ApiResponse.<List<PromotionCampaignSummaryDTO>>builder()
                                .code(HttpStatus.OK.value())
                                .message("Current promotion campaigns retrieved successfully")
                                .result(response)
                                .build();
        }

        @Operation(summary = "Get current campaigns with promotions", description = "Retrieve campaigns currently running with full promotion details (sorted by latest first)")
        @GetMapping("/current-with-promotions")
        public ApiResponse<List<PromotionCampaignResponseDTO>> getCurrentCampaignsWithPromotions() {
                List<PromotionCampaignResponseDTO> response = campaignService.getCurrentCampaignsWithPromotions();
                return ApiResponse.<List<PromotionCampaignResponseDTO>>builder()
                                .code(HttpStatus.OK.value())
                                .message("Current promotion campaigns with promotions retrieved successfully")
                                .result(response)
                                .build();
        }

        @Operation(summary = "Search campaigns by title", description = "Search promotion campaigns by title")
        @GetMapping("/search")
        public ApiResponse<Page<PromotionCampaignResponseDTO>> searchCampaigns(
                        @Parameter(description = "Search title") @RequestParam String title,
                        @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

                Pageable pageable = PageRequest.of(page, size, Sort.by("campaignId").descending());
                Page<PromotionCampaignResponseDTO> response = campaignService.searchCampaignsByTitle(title, pageable);
                return ApiResponse.<Page<PromotionCampaignResponseDTO>>builder()
                                .code(HttpStatus.OK.value())
                                .message("Promotion campaigns retrieved successfully")
                                .result(response)
                                .build();
        }

        @Operation(summary = "Filter campaigns by date range", description = "Get campaigns filtered by date range")
        @GetMapping("/filter-by-date")
        public ApiResponse<Page<PromotionCampaignResponseDTO>> getCampaignsByDateRange(
                        @Parameter(description = "Start date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                        @Parameter(description = "End date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                        @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

                Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").descending());
                Page<PromotionCampaignResponseDTO> response = campaignService.getCampaignsByDateRange(startDate,
                                endDate,
                                pageable);
                return ApiResponse.<Page<PromotionCampaignResponseDTO>>builder()
                                .code(HttpStatus.OK.value())
                                .message("Promotion campaigns retrieved successfully")
                                .result(response)
                                .build();
        }

        @Operation(summary = "Update campaign", description = "Update an existing promotion campaign")
        @PutMapping(value = "/{campaignId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        @PreAuthorize("hasAuthority('MANAGE_PROMOTIONS') or hasRole('ADMIN')")
        public ApiResponse<PromotionCampaignResponseDTO> updateCampaign(
                        @Parameter(description = "Campaign ID") @PathVariable Long campaignId,
                        @RequestParam("title") String title,
                        @RequestParam(value = "description", required = false) String description,
                        @RequestParam(value = "banner", required = false) MultipartFile banner,
                        @RequestParam("startDate") String startDate,
                        @RequestParam("endDate") String endDate,
                        @RequestParam(value = "active", defaultValue = "true") Boolean active,
                        HttpServletRequest request) {

                try {
                        // Create DTO manually from request parameters
                        PromotionCampaignUpdateDTO updateDTO = new PromotionCampaignUpdateDTO();
                        updateDTO.setTitle(title);
                        updateDTO.setDescription(description);
                        updateDTO.setBanner(banner);
                        updateDTO.setStartDate(LocalDate.parse(startDate));
                        updateDTO.setEndDate(LocalDate.parse(endDate));
                        updateDTO.setActive(active);

                        // Parse promotions from request parameters (for update)
                        List<CampaignPromotionDTO> promotions = parsePromotionsFromRequest(request);
                        updateDTO.setPromotions(promotions);

                        // Parse existing promotion IDs
                        List<Long> existingPromotionIds = parseExistingPromotionIds(request);
                        updateDTO.setExistingPromotionIds(existingPromotionIds);
                        
                        // Debug logging
                        System.out.println("=== Campaign Update Debug Info ===");
                        System.out.println("Campaign ID: " + campaignId);
                        System.out.println("Title: " + title);
                        System.out.println("New Promotions Count: " + (promotions != null ? promotions.size() : 0));
                        System.out.println("Existing Promotion IDs: " + existingPromotionIds);
                        System.out.println("==================================");

                        PromotionCampaignResponseDTO response = campaignService.updateCampaign(campaignId, updateDTO);
                        return ApiResponse.<PromotionCampaignResponseDTO>builder()
                                        .code(HttpStatus.OK.value())
                                        .message("Promotion campaign updated successfully")
                                        .result(response)
                                        .build();
                } catch (IllegalArgumentException e) {
                        return ApiResponse.<PromotionCampaignResponseDTO>builder()
                                        .code(HttpStatus.BAD_REQUEST.value())
                                        .message("Validation error: " + e.getMessage())
                                        .build();
                } catch (Exception e) {
                        return ApiResponse.<PromotionCampaignResponseDTO>builder()
                                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                        .message("Error updating campaign: " + e.getMessage())
                                        .build();
                }
        }

        @Operation(summary = "Delete campaign", description = "Delete a promotion campaign")
        @DeleteMapping("/{campaignId}")
        @PreAuthorize("hasAuthority('MANAGE_PROMOTIONS') or hasRole('ADMIN')")
        public ApiResponse<Void> deleteCampaign(
                        @Parameter(description = "Campaign ID") @PathVariable Long campaignId) {
                campaignService.deleteCampaign(campaignId);
                return ApiResponse.<Void>builder()
                                .code(HttpStatus.NO_CONTENT.value())
                                .message("Promotion campaign soft deleted successfully")
                                .build();
        }

        @Operation(summary = "Restore campaign", description = "Restore a soft deleted promotion campaign")
        @PostMapping("/{campaignId}/restore")
        @PreAuthorize("hasAuthority('MANAGE_PROMOTIONS') or hasRole('ADMIN')")
        public ApiResponse<PromotionCampaignResponseDTO> restoreCampaign(
                        @Parameter(description = "Campaign ID") @PathVariable Long campaignId) {
                PromotionCampaignResponseDTO response = campaignService.restoreCampaign(campaignId);
                return ApiResponse.<PromotionCampaignResponseDTO>builder()
                                .code(HttpStatus.OK.value())
                                .message("Promotion campaign restored successfully")
                                .result(response)
                                .build();
        }

        @Operation(summary = "Toggle campaign status", description = "Activate or deactivate a promotion campaign")
        @PatchMapping("/{campaignId}/toggle-status")
        @PreAuthorize("hasAuthority('MANAGE_PROMOTIONS') or hasRole('ADMIN')")
        public ApiResponse<PromotionCampaignResponseDTO> toggleCampaignStatus(
                        @Parameter(description = "Campaign ID") @PathVariable Long campaignId) {
                PromotionCampaignResponseDTO response = campaignService.toggleCampaignStatus(campaignId);
                return ApiResponse.<PromotionCampaignResponseDTO>builder()
                                .code(HttpStatus.OK.value())
                                .message("Promotion campaign status toggled successfully")
                                .result(response)
                                .build();
        }

        @Operation(summary = "Get campaigns ending soon", description = "Get campaigns ending within specified days")
        @GetMapping("/ending-soon")
        @PreAuthorize("hasAuthority('VIEW_PROMOTIONS') or hasRole('ADMIN')")
        public ApiResponse<List<PromotionCampaignSummaryDTO>> getCampaignsEndingSoon(
                        @Parameter(description = "Number of days") @RequestParam(defaultValue = "7") int days) {
                List<PromotionCampaignSummaryDTO> response = campaignService.getCampaignsEndingSoon(days);
                return ApiResponse.<List<PromotionCampaignSummaryDTO>>builder()
                                .code(HttpStatus.OK.value())
                                .message("Promotion campaigns ending soon retrieved successfully")
                                .result(response)
                                .build();
        }

        @Operation(summary = "Get campaign statistics", description = "Get overall campaign statistics")
        @GetMapping("/stats")
        @PreAuthorize("hasAuthority('VIEW_PROMOTIONS') or hasRole('ADMIN')")
        public ApiResponse<PromotionCampaignService.CampaignStatsDTO> getCampaignStats() {
                PromotionCampaignService.CampaignStatsDTO response = campaignService.getCampaignStats();
                return ApiResponse.<PromotionCampaignService.CampaignStatsDTO>builder()
                                .code(HttpStatus.OK.value())
                                .message("Promotion campaign statistics retrieved successfully")
                                .result(response)
                                .build();
        }

        /**
         * Parse promotions data from form request parameters
         * Format: promotions[0].discountType, promotions[0].promotionType, etc.
         */
        private List<CampaignPromotionDTO> parsePromotionsFromRequest(HttpServletRequest request) {
                List<CampaignPromotionDTO> promotions = new ArrayList<>();

                // Count number of promotions by checking for index pattern
                int index = 0;
                while (request.getParameter("promotions[" + index + "].discountType") != null) {
                        CampaignPromotionDTO promotion = new CampaignPromotionDTO();

                        // Required fields
                        String discountType = request.getParameter("promotions[" + index + "].discountType");
                        String promotionType = request.getParameter("promotions[" + index + "].promotionType");
                        String discountValue = request.getParameter("promotions[" + index + "].discountValue");
                        String status = request.getParameter("promotions[" + index + "].status");

                        if (discountType != null) {
                                promotion.setDiscountType(DiscountType.valueOf(discountType.toUpperCase()));
                        }
                        if (promotionType != null) {
                                promotion.setPromotionType(PromotionType.valueOf(promotionType.toLowerCase()));
                        }
                        if (discountValue != null) {
                                promotion.setDiscountValue(new BigDecimal(discountValue));
                        }
                        if (status != null) {
                                promotion.setStatus(PromotionStatus.valueOf(status.toLowerCase()));
                        }

                        // Optional fields
                        String minOrderValue = request.getParameter("promotions[" + index + "].minOrderValue");
                        if (minOrderValue != null && !minOrderValue.isEmpty()) {
                                promotion.setMinOrderValue(new BigDecimal(minOrderValue));
                        }

                        String usageLimit = request.getParameter("promotions[" + index + "].usageLimit");
                        if (usageLimit != null && !usageLimit.isEmpty()) {
                                promotion.setUsageLimit(Integer.parseInt(usageLimit));
                        }

                        String priority = request.getParameter("promotions[" + index + "].priority");
                        if (priority != null && !priority.isEmpty()) {
                                promotion.setPriority(Integer.parseInt(priority));
                        }

                        promotions.add(promotion);
                        index++;
                }

                return promotions;
        }

        /**
         * Parse existing promotion IDs from form request parameters
         * Format: existingPromotionIds[0], existingPromotionIds[1], etc.
         */
        private List<Long> parseExistingPromotionIds(HttpServletRequest request) {
                List<Long> promotionIds = new ArrayList<>();

                // Count number of existing promotion IDs by checking for index pattern
                int index = 0;
                while (request.getParameter("existingPromotionIds[" + index + "]") != null) {
                        String promotionIdStr = request.getParameter("existingPromotionIds[" + index + "]");
                        if (promotionIdStr != null && !promotionIdStr.isEmpty()) {
                                try {
                                        Long promotionId = Long.parseLong(promotionIdStr);
                                        promotionIds.add(promotionId);
                                } catch (NumberFormatException e) {
                                        throw new IllegalArgumentException("Invalid promotion ID: " + promotionIdStr);
                                }
                        }
                        index++;
                }

                return promotionIds;
        }
}