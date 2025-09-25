package com.busify.project.promotion.repository;

import com.busify.project.promotion.entity.PromotionCondition;

import io.lettuce.core.dynamic.annotation.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionConditionRepository extends JpaRepository<PromotionCondition, Long> {
    @Query("SELECT pc FROM PromotionCondition pc WHERE pc.promotion.promotionId = :promotionId")
    List<PromotionCondition> findPromotionByPromotionId(@Param("promotionId") Long promotionId);

    @Modifying
    @Query("DELETE FROM PromotionCondition pc WHERE pc.promotion.promotionId = :promotionId")
    void deleteConditionByPromotionId(@Param("promotionId") Long promotionId);

    @Query("SELECT pc FROM PromotionCondition pc WHERE pc.id = :conditionId")
    void deleteConditionById(@Param("conditionId") Long conditionId);
}