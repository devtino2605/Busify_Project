package com.busify.project.promotion.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.busify.project.promotion.enums.DiscountType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "promotion_campaigns")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionCampaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campaignId;

    private String title;
    private String description;
    private String bannerUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active = true;

    // Campaign-level discount value and its type (percentage or fixed amount)
    private BigDecimal discountValue = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType = DiscountType.FIXED_AMOUNT;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean deleted = false;

    @OneToMany(mappedBy = "campaign")
    private List<Promotion> promotions = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (deleted == null) {
            deleted = false;
        }
        if (active == null) {
            active = true;
        }
    }
}