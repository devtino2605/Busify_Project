package com.busify.project.promotion.repository;

import com.busify.project.promotion.entity.Promotion;
import com.busify.project.promotion.enums.PromotionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long>, JpaSpecificationExecutor<Promotion> {
        @Query("SELECT p FROM Promotion p WHERE p.code = :code")
        Optional<Promotion> findByCode(@Param("code") String code);

        @Query("SELECT p FROM Promotion p WHERE p.endDate <= CURRENT_DATE AND p.status <> 'expired'")
        List<Promotion> findAllExpiredButNotUpdated();

        // Methods mới cho AUTO promotion
        @Query("SELECT p FROM Promotion p WHERE p.promotionType = :type AND p.status = 'active' " +
                        "AND p.startDate <= CURRENT_DATE AND p.endDate >= CURRENT_DATE " +
                        "AND (p.minOrderValue IS NULL OR p.minOrderValue <= :orderValue) " +
                        "ORDER BY p.priority DESC, p.discountValue DESC")
        List<Promotion> findActivePromotionsByType(@Param("type") PromotionType type,
                        @Param("orderValue") BigDecimal orderValue);

        @Query("SELECT p FROM Promotion p WHERE p.promotionType = 'auto' AND p.status = 'active' " +
                        "AND p.startDate <= CURRENT_DATE AND p.endDate >= CURRENT_DATE " +
                        "AND (p.minOrderValue IS NULL OR p.minOrderValue <= :orderValue) " +
                        "ORDER BY p.priority DESC, p.discountValue DESC")
        List<Promotion> findActiveAutoPromotions(@Param("orderValue") BigDecimal orderValue);

        // Get all promotions in in current date range
        @Query("SELECT p FROM Promotion p WHERE p.startDate <= CURRENT_DATE AND p.endDate >= CURRENT_DATE AND p.status = 'active' ORDER BY p.priority DESC, p.discountValue DESC")
        List<Promotion> findAllCurrentPromotions();

        // Get auto promotions with completed conditions for specific user
        @Query("SELECT DISTINCT p FROM Promotion p " +
                        "WHERE p.promotionType = 'auto' " +
                        "AND p.status = 'active' " +
                        "AND p.startDate <= CURRENT_DATE " +
                        "AND p.endDate >= CURRENT_DATE " +
                        "AND (p.usageLimit IS NULL OR p.usageLimit > (SELECT COUNT(up2) FROM UserPromotion up2 WHERE up2.promotion.promotionId = p.promotionId AND up2.isUsed = true)) "
                        +
                        "AND NOT EXISTS (SELECT 1 FROM PromotionCondition pc WHERE pc.promotion.promotionId = p.promotionId AND pc.isRequired = true "
                        +
                        "AND NOT EXISTS (SELECT 1 FROM UserPromotionCondition upc WHERE upc.promotionCondition.id = pc.id AND upc.user.id = :userId AND upc.isCompleted = true)) "
                        +
                        "ORDER BY p.priority DESC, p.discountValue DESC")
        List<Promotion> findAutoPromotionsWithCompletedConditionsForUser(@Param("userId") Long userId);
}