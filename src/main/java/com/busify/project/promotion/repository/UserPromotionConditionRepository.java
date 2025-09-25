package com.busify.project.promotion.repository;

import com.busify.project.promotion.entity.UserPromotionCondition;

import io.lettuce.core.dynamic.annotation.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPromotionConditionRepository extends JpaRepository<UserPromotionCondition, Long> {
        @Query("SELECT upc FROM UserPromotionCondition upc WHERE upc.user.id = :userId AND upc.promotionCondition.promotion.promotionId = :promotionId")
        List<UserPromotionCondition> findPromotionsConditionByUserAndPromotionId(@Param("userId") Long userId,
                        @Param("promotionId") Long promotionId);

        @Query("SELECT upc FROM UserPromotionCondition upc WHERE upc.user.id = :userId AND upc.promotionCondition.id = :conditionId")
        Optional<UserPromotionCondition> findUserPromotionConditionByUserAndConditionId(@Param("userId") Long userId,
                        @Param("conditionId") Long conditionId);

        @Query("SELECT CASE WHEN COUNT(upc) > 0 THEN true ELSE false END FROM UserPromotionCondition upc WHERE upc.user.id = :userId AND upc.promotionCondition.id = :conditionId AND upc.isCompleted = :isCompleted")
        boolean existsByUserIdAndConditionIdAdIsCompleted(Long userId, Long conditionId, Boolean isCompleted);

        @Query("SELECT upc FROM UserPromotionCondition upc WHERE upc.user.id = :userId")
        List<UserPromotionCondition> findAllPromotionConditionsByUserId(@Param("userId") Long userId);
}