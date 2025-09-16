package com.busify.project.promotion.service.impl;

import com.busify.project.promotion.dto.campaign.CampaignPromotionDTO;
import com.busify.project.promotion.dto.campaign.PromotionCampaignCreateDTO;
import com.busify.project.promotion.dto.campaign.PromotionCampaignFilterResponseDTO;
import com.busify.project.promotion.dto.campaign.PromotionCampaignResponseDTO;
import com.busify.project.promotion.dto.campaign.PromotionCampaignSummaryDTO;
import com.busify.project.promotion.dto.campaign.PromotionCampaignUpdateDTO;
import com.busify.project.promotion.entity.Promotion;
import com.busify.project.promotion.entity.PromotionCampaign;
import com.busify.project.promotion.enums.PromotionType;
import com.busify.project.promotion.exception.PromotionCampaignException;
import com.busify.project.promotion.mapper.PromotionCampaignMapper;
import com.busify.project.promotion.repository.PromotionCampaignRepository;
import com.busify.project.promotion.repository.PromotionRepository;
import com.busify.project.promotion.service.PromotionCampaignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PromotionCampaignServiceImpl implements PromotionCampaignService {

    private final PromotionCampaignRepository campaignRepository;
    private final PromotionRepository promotionRepository;
    private final PromotionCampaignMapper campaignMapper;

    @Override
    public PromotionCampaignResponseDTO createCampaign(PromotionCampaignCreateDTO createDTO) {
        log.info("Creating new promotion campaign: {}", createDTO.getTitle());

        // Validate date range
        validateDateRange(createDTO.getStartDate(), createDTO.getEndDate());

        // Convert DTO to entity
        PromotionCampaign campaign = campaignMapper.toEntity(createDTO);

        // Save campaign first
        PromotionCampaign savedCampaign = campaignRepository.save(campaign);
        log.info("Promotion campaign created successfully with ID: {}", savedCampaign.getCampaignId());

        // Create promotions if provided
        if (createDTO.getPromotions() != null && !createDTO.getPromotions().isEmpty()) {
            createPromotionsForCampaign(savedCampaign, createDTO.getPromotions());
            log.info("Created {} promotions for campaign {}", createDTO.getPromotions().size(),
                    savedCampaign.getCampaignId());
        }

        // Link existing promotions if provided
        if (createDTO.getExistingPromotionIds() != null && !createDTO.getExistingPromotionIds().isEmpty()) {
            linkExistingPromotionsToCampaign(savedCampaign, createDTO.getExistingPromotionIds());
            log.info("Linked {} existing promotions to campaign {}", createDTO.getExistingPromotionIds().size(),
                    savedCampaign.getCampaignId());
        }

        // Reload campaign with promotions
        savedCampaign = campaignRepository.findById(savedCampaign.getCampaignId())
                .orElse(savedCampaign);

        return campaignMapper.toResponseDTO(savedCampaign);
    }

    @Override
    @Transactional(readOnly = true)
    public PromotionCampaignResponseDTO getCampaignById(Long campaignId) {
        log.debug("Retrieving campaign with ID: {}", campaignId);

        PromotionCampaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> PromotionCampaignException.notFound(campaignId));

        return campaignMapper.toResponseDTO(campaign);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PromotionCampaignResponseDTO> getAllCampaigns(Pageable pageable) {
        log.debug("Retrieving all campaigns with pagination");

        Page<PromotionCampaign> campaigns = campaignRepository.findAll(pageable);
        return campaigns.map(campaignMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PromotionCampaignFilterResponseDTO getAllCampaignsSimple(int page, int size, String sortBy, String sortDir) {
        log.debug("Retrieving all campaigns with simple pagination");

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<PromotionCampaign> campaigns = campaignRepository.findAll(pageable);

        List<PromotionCampaignResponseDTO> campaignDTOs = campaigns.getContent().stream()
                .map(campaignMapper::toResponseDTO)
                .collect(Collectors.toList());

        return PromotionCampaignFilterResponseDTO.builder()
                .campaigns(campaignDTOs)
                .currentPage(campaigns.getNumber() + 1) // Convert từ 0-based sang 1-based
                .totalPages(campaigns.getTotalPages())
                .totalElements(campaigns.getTotalElements())
                .pageSize(campaigns.getSize())
                .hasNext(campaigns.hasNext())
                .hasPrevious(campaigns.hasPrevious())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionCampaignSummaryDTO> getActiveCampaigns() {
        log.debug("Retrieving all active campaigns");

        List<PromotionCampaign> activeCampaigns = campaignRepository.findByActiveTrue();
        return activeCampaigns.stream()
                .map(campaignMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionCampaignSummaryDTO> getCurrentCampaigns() {
        log.debug("Retrieving current running campaigns");

        LocalDate currentDate = LocalDate.now();
        List<PromotionCampaign> currentCampaigns = campaignRepository.findActiveCampaignsInDateRange(currentDate);
        return currentCampaigns.stream()
                .map(campaignMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionCampaignResponseDTO> getCurrentCampaignsWithPromotions() {
        log.debug("Retrieving current running campaigns with full promotions details");

        LocalDate currentDate = LocalDate.now();
        List<PromotionCampaign> currentCampaigns = campaignRepository.findActiveCampaignsInDateRange(currentDate);
        return currentCampaigns.stream()
                .map(campaignMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PromotionCampaignResponseDTO> searchCampaignsByTitle(String title, Pageable pageable) {
        log.debug("Searching campaigns by title: {}", title);

        Page<PromotionCampaign> campaigns = campaignRepository.findByTitleContainingIgnoreCase(title, pageable);
        return campaigns.map(campaignMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PromotionCampaignResponseDTO> getCampaignsByDateRange(LocalDate startDate, LocalDate endDate,
            Pageable pageable) {
        log.debug("Retrieving campaigns by date range: {} to {}", startDate, endDate);

        Page<PromotionCampaign> campaigns = campaignRepository.findByDateRange(startDate, endDate, pageable);
        return campaigns.map(campaignMapper::toResponseDTO);
    }

    @Override
    public PromotionCampaignResponseDTO updateCampaign(Long campaignId, PromotionCampaignUpdateDTO updateDTO) {
        log.info("Updating campaign with ID: {}", campaignId);

        // Validate date range
        validateDateRange(updateDTO.getStartDate(), updateDTO.getEndDate());

        // Get existing campaign
        PromotionCampaign existingCampaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> PromotionCampaignException.notFound(campaignId));

        // Update campaign basic info
        campaignMapper.updateEntity(existingCampaign, updateDTO);

        // Save updated campaign first to get new dates
        PromotionCampaign updatedCampaign = campaignRepository.save(existingCampaign);

        // Handle promotions update - only if explicitly provided
        boolean promotionsModified = false;

        // Step 1: Handle existing promotion IDs (these are the promotions we want to
        // keep/link)
        if (updateDTO.getExistingPromotionIds() != null) {
            // Get current promotion IDs linked to this campaign
            List<Long> currentPromotionIds = updatedCampaign.getPromotions() != null
                    ? updatedCampaign.getPromotions().stream()
                            .map(Promotion::getPromotionId)
                            .collect(java.util.stream.Collectors.toList())
                    : new ArrayList<>();

            List<Long> newPromotionIds = updateDTO.getExistingPromotionIds();

            log.info("Campaign {} current promotion IDs: {}", campaignId, currentPromotionIds);
            log.info("Campaign {} new promotion IDs: {}", campaignId, newPromotionIds);

            // Find promotions to unlink (in current but not in new list)
            List<Long> toUnlink = currentPromotionIds.stream()
                    .filter(id -> !newPromotionIds.contains(id))
                    .collect(java.util.stream.Collectors.toList());

            // Find promotions to link (in new list but not in current)
            List<Long> toLink = newPromotionIds.stream()
                    .filter(id -> !currentPromotionIds.contains(id))
                    .collect(java.util.stream.Collectors.toList());

            log.info("Campaign {} promotions to unlink: {}", campaignId, toUnlink);
            log.info("Campaign {} promotions to link: {}", campaignId, toLink);

            // Unlink promotions that are no longer needed
            if (!toUnlink.isEmpty()) {
                List<Long> actuallyUnlinked = new ArrayList<>();
                List<Long> cannotUnlink = new ArrayList<>();

                for (Long promotionId : toUnlink) {
                    if (isPromotionSafeToUnlink(promotionId)) {
                        Promotion promotion = promotionRepository.findById(promotionId).orElse(null);
                        if (promotion != null && promotion.getCampaign() != null
                                && promotion.getCampaign().getCampaignId().equals(campaignId)) {
                            promotion.setCampaign(null); // Unlink instead of delete
                            promotionRepository.save(promotion);
                            actuallyUnlinked.add(promotionId);
                            log.info("Unlinked promotion {} from campaign {}", promotionId, campaignId);
                        }
                    } else {
                        cannotUnlink.add(promotionId);
                    }
                }

                if (!cannotUnlink.isEmpty()) {
                    log.warn("Could not unlink {} promotions from campaign {} as they have been used in bookings: {}",
                            cannotUnlink.size(), campaignId, cannotUnlink);
                }

                if (!actuallyUnlinked.isEmpty()) {
                    promotionsModified = true;
                }
            }

            // Link new promotions
            if (!toLink.isEmpty()) {
                linkExistingPromotionsToCampaign(updatedCampaign, toLink);
                log.info("Linked {} new promotions to campaign {}", toLink.size(), campaignId);
                promotionsModified = true;
            }

            log.info("Updated campaign {} promotion links: {} unlinked, {} linked",
                    campaignId, toUnlink.size(), toLink.size());
        }

        // Step 2: Create new promotions if provided
        if (updateDTO.getPromotions() != null && !updateDTO.getPromotions().isEmpty()) {
            createPromotionsForCampaign(updatedCampaign, updateDTO.getPromotions());
            log.info("Created {} new promotions for campaign {}", updateDTO.getPromotions().size(), campaignId);
            promotionsModified = true;
        }

        // Reload campaign with updated promotions only if we modified them
        if (promotionsModified) {
            updatedCampaign = campaignRepository.findById(campaignId)
                    .orElse(updatedCampaign);
        }

        log.info("Campaign updated successfully with ID: {}", updatedCampaign.getCampaignId());

        return campaignMapper.toResponseDTO(updatedCampaign);
    }

    @Override
    public void deleteCampaign(Long campaignId) {
        log.info("Soft deleting campaign with ID: {}", campaignId);

        PromotionCampaign campaign = campaignRepository.findByIdIncludingDeleted(campaignId)
                .orElseThrow(() -> PromotionCampaignException.notFound(campaignId));

        // Check if already deleted
        if (campaign.getDeleted()) {
            throw new IllegalStateException("Campaign is already deleted: " + campaignId);
        }

        // Check if campaign has active promotions - warn but allow deletion
        if (campaign.getPromotions() != null && !campaign.getPromotions().isEmpty()) {
            boolean hasActivePromotions = campaign.getPromotions().stream()
                    .anyMatch(promotion -> promotion.getStatus() != null &&
                            promotion.getStatus().toString().equalsIgnoreCase("active"));
            if (hasActivePromotions) {
                log.warn("Campaign {} has {} active promotions. They will be unlinked from campaign.",
                        campaignId, campaign.getPromotions().size());
            }

            // Unlink all promotions from this campaign (Option 2: unlink promotions)
            for (Promotion promotion : campaign.getPromotions()) {
                promotion.setCampaign(null);
                promotionRepository.save(promotion);
                log.info("Unlinked promotion {} from campaign {}", promotion.getPromotionId(), campaignId);
            }
        }

        // Soft delete campaign
        campaign.setDeleted(true);
        campaignRepository.save(campaign);

        log.info("Campaign soft deleted successfully with ID: {}", campaignId);
    }

    @Override
    public PromotionCampaignResponseDTO restoreCampaign(Long campaignId) {
        log.info("Restoring campaign with ID: {}", campaignId);

        PromotionCampaign campaign = campaignRepository.findByIdIncludingDeleted(campaignId)
                .orElseThrow(() -> PromotionCampaignException.notFound(campaignId));

        // Check if not deleted
        if (!campaign.getDeleted()) {
            throw new IllegalStateException("Campaign is not deleted: " + campaignId);
        }

        // Restore campaign
        campaign.setDeleted(false);
        PromotionCampaign restoredCampaign = campaignRepository.save(campaign);

        log.info("Campaign restored successfully with ID: {}", campaignId);
        return campaignMapper.toResponseDTO(restoredCampaign);
    }

    @Override
    public PromotionCampaignResponseDTO toggleCampaignStatus(Long campaignId) {
        log.info("Toggling status for campaign with ID: {}", campaignId);

        PromotionCampaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> PromotionCampaignException.notFound(campaignId));

        campaign.setActive(!campaign.getActive());
        PromotionCampaign updatedCampaign = campaignRepository.save(campaign);

        log.info("Campaign status toggled successfully. ID: {}, New Status: {}",
                campaignId, updatedCampaign.getActive());

        return campaignMapper.toResponseDTO(updatedCampaign);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionCampaignSummaryDTO> getCampaignsEndingSoon(int days) {
        log.debug("Retrieving campaigns ending within {} days", days);

        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = currentDate.plusDays(days);

        List<PromotionCampaign> endingCampaigns = campaignRepository.findCampaignsEndingSoon(currentDate, endDate);
        return endingCampaigns.stream()
                .map(campaignMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CampaignStatsDTO getCampaignStats() {
        log.debug("Retrieving campaign statistics");

        long totalCampaigns = campaignRepository.count();
        long activeCampaigns = campaignRepository.countByActiveTrue();

        LocalDate currentDate = LocalDate.now();
        List<PromotionCampaign> allCampaigns = campaignRepository.findAll();

        long expiredCampaigns = allCampaigns.stream()
                .filter(campaign -> campaign.getEndDate().isBefore(currentDate))
                .count();

        long upcomingCampaigns = allCampaigns.stream()
                .filter(campaign -> campaign.getStartDate().isAfter(currentDate))
                .count();

        return new CampaignStatsDTO(totalCampaigns, activeCampaigns, expiredCampaigns, upcomingCampaigns);
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw PromotionCampaignException.invalidDateRange();
        }
    }

    /**
     * Create promotions for a campaign
     */
    private void createPromotionsForCampaign(PromotionCampaign campaign, List<CampaignPromotionDTO> promotionDTOs) {
        for (CampaignPromotionDTO promotionDTO : promotionDTOs) {
            Promotion promotion = new Promotion();

            // Set promotion fields from DTO
            promotion.setDiscountType(promotionDTO.getDiscountType());
            promotion.setPromotionType(promotionDTO.getPromotionType());
            promotion.setDiscountValue(promotionDTO.getDiscountValue());
            promotion.setMinOrderValue(promotionDTO.getMinOrderValue());
            promotion.setUsageLimit(promotionDTO.getUsageLimit());
            promotion.setStatus(promotionDTO.getStatus());
            promotion.setPriority(promotionDTO.getPriority() != null ? promotionDTO.getPriority() : 0);

            // Set dates from campaign
            promotion.setStartDate(campaign.getStartDate());
            promotion.setEndDate(campaign.getEndDate());

            // Link to campaign
            promotion.setCampaign(campaign);

            // Generate code for coupon type
            if (promotionDTO.getPromotionType() == PromotionType.coupon) {
                // Generate unique code for coupon
                String code = generatePromotionCode(campaign.getTitle());
                promotion.setCode(code);
            } else {
                // Auto type doesn't need code
                promotion.setCode(null);
            }

            // Save promotion
            promotionRepository.save(promotion);
        }
    }

    /**
     * Link existing promotions to a campaign
     */
    private void linkExistingPromotionsToCampaign(PromotionCampaign campaign, List<Long> promotionIds) {
        for (Long promotionId : promotionIds) {
            Promotion existingPromotion = promotionRepository.findById(promotionId)
                    .orElseThrow(() -> new IllegalArgumentException("Promotion not found with ID: " + promotionId));

            // Validate promotion dates are compatible with campaign dates
            if (existingPromotion.getStartDate().isBefore(campaign.getStartDate()) ||
                    existingPromotion.getEndDate().isAfter(campaign.getEndDate())) {
                log.warn(
                        "Promotion {} dates ({} to {}) extend beyond campaign dates ({} to {}). Adjusting to campaign dates.",
                        promotionId, existingPromotion.getStartDate(), existingPromotion.getEndDate(),
                        campaign.getStartDate(), campaign.getEndDate());

                // Adjust promotion dates to fit within campaign dates
                if (existingPromotion.getStartDate().isBefore(campaign.getStartDate())) {
                    existingPromotion.setStartDate(campaign.getStartDate());
                }
                if (existingPromotion.getEndDate().isAfter(campaign.getEndDate())) {
                    existingPromotion.setEndDate(campaign.getEndDate());
                }
            }

            // Link promotion to campaign
            existingPromotion.setCampaign(campaign);
            promotionRepository.save(existingPromotion);
        }
    }

    /**
     * Check if promotion is safe to unlink (not used in bookings)
     */
    private boolean isPromotionSafeToUnlink(Long promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId).orElse(null);
        if (promotion == null)
            return true;

        // Check if promotion has been used in any bookings
        boolean hasBookings = promotion.getBookings() != null && !promotion.getBookings().isEmpty();

        if (hasBookings) {
            log.warn("Promotion {} cannot be unlinked as it has been used in {} bookings",
                    promotionId, promotion.getBookings().size());
            return false;
        }

        return true;
    }

    /**
     * Generate unique promotion code based on campaign title
     */
    private String generatePromotionCode(String campaignTitle) {
        // Simple code generation - you can make this more sophisticated
        String prefix = campaignTitle.replaceAll("[^A-Za-z0-9]", "")
                .toUpperCase()
                .substring(0, Math.min(6, campaignTitle.length()));

        // Add timestamp to ensure uniqueness
        String timestamp = String.valueOf(System.currentTimeMillis() % 100000);

        return prefix + timestamp;
    }

    @Override
    @Scheduled(cron = "0 30 0 * * *") // Chạy mỗi ngày lúc 0:30 (sau khi promotion scheduler chạy)
    public void updateStatusExpiredCampaigns() {
        LocalDate currentDate = LocalDate.now();
        List<PromotionCampaign> expiredCampaigns = campaignRepository.findExpiredButActiveCampaigns(currentDate);

        for (PromotionCampaign campaign : expiredCampaigns) {
            campaign.setActive(false);
            log.info("Campaign expired and deactivated: {} (ID: {})", campaign.getTitle(), campaign.getCampaignId());
        }

        if (!expiredCampaigns.isEmpty()) {
            campaignRepository.saveAll(expiredCampaigns);
            log.info("Updated {} expired campaigns to inactive status", expiredCampaigns.size());
        }
    }
}
