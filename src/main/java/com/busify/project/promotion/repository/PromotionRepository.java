package com.busify.project.promotion.repository;

import com.busify.project.promotion.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion,Long> {
    @Query("SELECT p FROM Promotion p WHERE p.code = :code")
    Optional<Promotion> findByCode(@Param("code") String code);

    @Query("SELECT p FROM Promotion p WHERE p.endDate <= CURRENT_DATE AND p.status <> 'expired'")
    java.util.List<Promotion> findAllExpiredButNotUpdated();
}