package com.busify.project.promotion.repository;

import com.busify.project.promotion.entity.PromotionCampaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PromotionCampaignRepository extends JpaRepository<PromotionCampaign, Long> {

        /**
         * Override findAll to exclude deleted campaigns (handle NULL as false)
         */
        @Query("SELECT pc FROM PromotionCampaign pc")
        List<PromotionCampaign> findAll();

        /**
         * Override findAll with Pageable to exclude deleted campaigns (handle NULL as
         * false)
         */
        @Query("SELECT pc FROM PromotionCampaign pc")
        Page<PromotionCampaign> findAll(Pageable pageable);

        /**
         * Find all active and non-deleted campaigns (handle NULL as false)
         */
        @Query("SELECT pc FROM PromotionCampaign pc WHERE pc.active = true AND (pc.deleted IS NULL OR pc.deleted = false)")
        List<PromotionCampaign> findByActiveTrue();

        /**
         * Find non-deleted campaigns by title containing keyword (case insensitive,
         * handle NULL as false)
         */
        @Query("SELECT pc FROM PromotionCampaign pc WHERE (pc.deleted IS NULL OR pc.deleted = false) AND UPPER(pc.title) LIKE UPPER(CONCAT('%', :title, '%'))")
        Page<PromotionCampaign> findByTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);

        /**
         * Find active and non-deleted campaigns within date range (handle NULL as
         * false)
         * Sorted by startDate DESC for latest campaigns first
         */
        @Query("SELECT pc FROM PromotionCampaign pc WHERE pc.active = true AND (pc.deleted IS NULL OR pc.deleted = false) "
                        +
                        "AND pc.startDate <= :currentDate AND pc.endDate >= :currentDate ORDER BY pc.startDate DESC")
        List<PromotionCampaign> findActiveCampaignsInDateRange(@Param("currentDate") LocalDate currentDate);

        /**
         * Find non-deleted campaigns by date range (handle NULL as false)
         */
        @Query("SELECT pc FROM PromotionCampaign pc WHERE (pc.deleted IS NULL OR pc.deleted = false) AND " +
                        "(:startDate IS NULL OR pc.startDate >= :startDate) AND " +
                        "(:endDate IS NULL OR pc.endDate <= :endDate)")
        Page<PromotionCampaign> findByDateRange(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        Pageable pageable);

        /**
         * Count active and non-deleted campaigns (handle NULL as false)
         */
        @Query("SELECT COUNT(pc) FROM PromotionCampaign pc WHERE pc.active = true AND (pc.deleted IS NULL OR pc.deleted = false)")
        long countByActiveTrue();

        /**
         * Find non-deleted campaigns ending soon (within specified days, handle NULL as
         * false)
         */
        @Query("SELECT pc FROM PromotionCampaign pc WHERE pc.active = true AND (pc.deleted IS NULL OR pc.deleted = false) "
                        +
                        "AND pc.endDate BETWEEN :currentDate AND :endDate")
        List<PromotionCampaign> findCampaignsEndingSoon(@Param("currentDate") LocalDate currentDate,
                        @Param("endDate") LocalDate endDate);

        /**
         * Find campaign by ID including deleted ones (for admin operations)
         */
        @Query("SELECT pc FROM PromotionCampaign pc WHERE pc.campaignId = :campaignId")
        java.util.Optional<PromotionCampaign> findByIdIncludingDeleted(@Param("campaignId") Long campaignId);

        /**
         * Find campaigns that are active but have passed end date (for scheduler)
         */
        @Query("SELECT pc FROM PromotionCampaign pc WHERE pc.active = true AND pc.endDate < :currentDate AND (pc.deleted IS NULL OR pc.deleted = false)")
        List<PromotionCampaign> findExpiredButActiveCampaigns(@Param("currentDate") LocalDate currentDate);
}
