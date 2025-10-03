package com.busify.project.promotion.entity;

import com.busify.project.promotion.enums.ConditionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "promotion_conditions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConditionType conditionType;

    @Column(nullable = true)
    private String conditionValue; // e.g., video_id, survey_id, referral_count

    @Column(nullable = false)
    private Boolean isRequired = true; // Whether this condition must be met
}