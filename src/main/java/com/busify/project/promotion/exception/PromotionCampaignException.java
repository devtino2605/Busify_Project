package com.busify.project.promotion.exception;

public class PromotionCampaignException extends RuntimeException {

    public PromotionCampaignException(String message) {
        super(message);
    }

    public PromotionCampaignException(String message, Throwable cause) {
        super(message, cause);
    }

    // Campaign not found
    public static PromotionCampaignException notFound(Long campaignId) {
        return new PromotionCampaignException("Promotion campaign not found with ID: " + campaignId);
    }

    // Campaign already exists
    public static PromotionCampaignException alreadyExists(String title) {
        return new PromotionCampaignException("Promotion campaign already exists with title: " + title);
    }

    // Invalid date range
    public static PromotionCampaignException invalidDateRange() {
        return new PromotionCampaignException("End date must be after start date");
    }

    // Campaign has active promotions
    public static PromotionCampaignException hasActivePromotions(Long campaignId) {
        return new PromotionCampaignException(
                "Cannot delete campaign with active promotions. Campaign ID: " + campaignId);
    }

    // Banner upload failed
    public static PromotionCampaignException bannerUploadFailed(String filename, Throwable cause) {
        return new PromotionCampaignException("Failed to upload campaign banner: " + filename, cause);
    }

    // Campaign is expired
    public static PromotionCampaignException expired(Long campaignId) {
        return new PromotionCampaignException("Campaign is expired. Campaign ID: " + campaignId);
    }

    // Campaign is not active
    public static PromotionCampaignException notActive(Long campaignId) {
        return new PromotionCampaignException("Campaign is not active. Campaign ID: " + campaignId);
    }
}
