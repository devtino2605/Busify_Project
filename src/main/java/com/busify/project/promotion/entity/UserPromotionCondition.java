package com.busify.project.promotion.entity;

import com.busify.project.user.entity.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_promotion_conditions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPromotionCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Profile user;

    @ManyToOne
    @JoinColumn(name = "promotion_condition_id", nullable = false)
    private PromotionCondition promotionCondition;

    @Column(nullable = false)
    private Boolean isCompleted = false;

    @Column
    private LocalDateTime completedAt;

    @Column
    private String progressData; // JSON string for additional progress info, e.g., {"video_viewed": true, "survey_answers": {...}}
}