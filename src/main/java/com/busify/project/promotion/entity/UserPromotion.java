package com.busify.project.promotion.entity;

import com.busify.project.user.entity.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_promotions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserPromotionId.class)
public class UserPromotion {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "promotion_id")
    private Long promotionId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private Profile user;

    @ManyToOne
    @JoinColumn(name = "promotion_id", insertable = false, updatable = false)
    private Promotion promotion;

    @Column(nullable = false)
    private LocalDateTime claimedAt;

    @Column
    private LocalDateTime usedAt;

    @Column(nullable = false)
    private Boolean isUsed = false;

    // Constructor for claiming promotion
    public UserPromotion(Profile user, Promotion promotion) {
        this.userId = user.getId();
        this.promotionId = promotion.getPromotionId();
        this.user = user;
        this.promotion = promotion;
        this.claimedAt = LocalDateTime.now();
        this.isUsed = false;
    }

    // Method to mark as used
    public void markAsUsed() {
        this.isUsed = true;
        this.usedAt = LocalDateTime.now();
    }

    // Method to remove mark as used
    public void removeMarkAsUsed() {
        this.isUsed = false;
        this.usedAt = null;
    }
}