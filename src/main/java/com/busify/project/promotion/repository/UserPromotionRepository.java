package com.busify.project.promotion.repository;

import com.busify.project.promotion.entity.UserPromotion;
import com.busify.project.promotion.entity.UserPromotionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPromotionRepository extends JpaRepository<UserPromotion, UserPromotionId> {
    
    // Tìm promotion đã được claim bởi user và chưa sử dụng
    @Query("SELECT up FROM UserPromotion up WHERE up.user.id = :userId AND up.promotion.code = :code AND up.isUsed = false")
    Optional<UserPromotion> findAvailablePromotionForUser(@Param("userId") Long userId, @Param("code") String code);

    // Tìm promotion đã được sử dụng bởi user
    @Query("SELECT up FROM UserPromotion up WHERE up.user.id = :userId AND up.promotion.code = :code AND up.isUsed = true")
    Optional<UserPromotion> findUsedPromotionForUser(@Param("userId") Long userId, @Param("code") String code);
    
    // Lấy danh sách promotion đã claim bởi user
    @Query("SELECT up FROM UserPromotion up WHERE up.user.id = :userId")
    List<UserPromotion> findAllByUserId(@Param("userId") Long userId);
    
    // Lấy danh sách promotion chưa sử dụng của user
    @Query("SELECT up FROM UserPromotion up WHERE up.user.id = :userId AND up.isUsed = false")
    List<UserPromotion> findUnusedPromotionsByUserId(@Param("userId") Long userId);

    // Lấy danh sách promotion đã sử dụng của user
    @Query("SELECT up FROM UserPromotion up WHERE up.user.id = :userId AND up.isUsed = true")
    List<UserPromotion> findUsedPromotionsByUserId(@Param("userId") Long userId);
    
    // Check xem user đã claim promotion này chưa
    @Query("SELECT COUNT(up) > 0 FROM UserPromotion up WHERE up.user.id = :userId AND up.promotion.promotionId = :promotionId")
    boolean existsByUserIdAndPromotionId(@Param("userId") Long userId, @Param("promotionId") Long promotionId);
    
    // Đếm số lượng đã sử dụng của một promotion
    @Query("SELECT COUNT(up) FROM UserPromotion up WHERE up.promotion.promotionId = :promotionId AND up.isUsed = true")
    long countUsedByPromotionId(@Param("promotionId") Long promotionId);
}