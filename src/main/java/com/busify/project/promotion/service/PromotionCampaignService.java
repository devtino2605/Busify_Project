package com.busify.project.promotion.service;

import com.busify.project.promotion.dto.campaign.PromotionCampaignCreateDTO;
import com.busify.project.promotion.dto.campaign.PromotionCampaignFilterResponseDTO;
import com.busify.project.promotion.dto.campaign.PromotionCampaignResponseDTO;
import com.busify.project.promotion.dto.campaign.PromotionCampaignSummaryDTO;
import com.busify.project.promotion.dto.campaign.PromotionCampaignUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PromotionCampaignService {

    /**
     * Create a new promotion campaign
     */
    PromotionCampaignResponseDTO createCampaign(PromotionCampaignCreateDTO createDTO);

    /**
     * Get campaign by ID
     */
    PromotionCampaignResponseDTO getCampaignById(Long campaignId);

    /**
     * Get all campaigns with pagination
     */
    Page<PromotionCampaignResponseDTO> getAllCampaigns(Pageable pageable);

    /**
     * Get all campaigns with simple pagination response
     */
    PromotionCampaignFilterResponseDTO getAllCampaignsSimple(int page, int size, String sortBy, String sortDir);

    /**
     * Get active campaigns
     */
    List<PromotionCampaignSummaryDTO> getActiveCampaigns();

    /**
     * Get campaigns currently running (within date range)
     */
    List<PromotionCampaignSummaryDTO> getCurrentCampaigns();

    /**
     * Get campaigns currently running with full promotion details (sorted by latest
     * first)
     */
    List<PromotionCampaignResponseDTO> getCurrentCampaignsWithPromotions();

    /**
     * Search campaigns by title
     */
    Page<PromotionCampaignResponseDTO> searchCampaignsByTitle(String title, Pageable pageable);

    /**
     * Filter campaigns by date range
     */
    Page<PromotionCampaignResponseDTO> getCampaignsByDateRange(LocalDate startDate, LocalDate endDate,
            Pageable pageable);

    /**
     * Update campaign
     */
    PromotionCampaignResponseDTO updateCampaign(Long campaignId, PromotionCampaignUpdateDTO updateDTO);

    /**
     * Delete campaign (soft delete)
     */
    void deleteCampaign(Long campaignId);

    /**
     * Restore soft deleted campaign
     */
    PromotionCampaignResponseDTO restoreCampaign(Long campaignId);

    /**
     * Activate/Deactivate campaign
     */
    PromotionCampaignResponseDTO toggleCampaignStatus(Long campaignId);

    /**
     * Get campaigns ending soon (within specified days)
     */
    List<PromotionCampaignSummaryDTO> getCampaignsEndingSoon(int days);

    /**
     * Get campaign statistics
     */
    CampaignStatsDTO getCampaignStats();

    /**
     * Update status of expired campaigns (scheduler method)
     */
    void updateStatusExpiredCampaigns();

    /**
     * Inner class for campaign statistics
     */
    class CampaignStatsDTO {
        private long totalCampaigns;
        private long activeCampaigns;
        private long expiredCampaigns;
        private long upcomingCampaigns;

        // Constructors, getters, setters
        public CampaignStatsDTO() {
        }

        public CampaignStatsDTO(long totalCampaigns, long activeCampaigns, long expiredCampaigns,
                long upcomingCampaigns) {
            this.totalCampaigns = totalCampaigns;
            this.activeCampaigns = activeCampaigns;
            this.expiredCampaigns = expiredCampaigns;
            this.upcomingCampaigns = upcomingCampaigns;
        }

        public long getTotalCampaigns() {
            return totalCampaigns;
        }

        public void setTotalCampaigns(long totalCampaigns) {
            this.totalCampaigns = totalCampaigns;
        }

        public long getActiveCampaigns() {
            return activeCampaigns;
        }

        public void setActiveCampaigns(long activeCampaigns) {
            this.activeCampaigns = activeCampaigns;
        }

        public long getExpiredCampaigns() {
            return expiredCampaigns;
        }

        public void setExpiredCampaigns(long expiredCampaigns) {
            this.expiredCampaigns = expiredCampaigns;
        }

        public long getUpcomingCampaigns() {
            return upcomingCampaigns;
        }

        public void setUpcomingCampaigns(long upcomingCampaigns) {
            this.upcomingCampaigns = upcomingCampaigns;
        }
    }
}
