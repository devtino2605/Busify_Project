package com.busify.project.promotion.repository;

import com.busify.project.promotion.entity.Promotion;
import com.busify.project.promotion.enums.PromotionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion,Long> {
    @Query("SELECT p FROM Promotion p WHERE p.code = :code")
    Optional<Promotion> findByCode(@Param("code") String code);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM promotion_user WHERE user_id = :userId AND promotion_id = :promotionId", nativeQuery = true)
    int existsUserUseCode(@Param("userId") Long userId, @Param("promotionId") Long promotionId);

    @Query("SELECT p FROM Promotion p WHERE p.endDate <= CURRENT_DATE AND p.status <> 'expired'")
    java.util.List<Promotion> findAllExpiredButNotUpdated();
}