package com.busify.project.promotion.entity;

import com.busify.project.booking.entity.Bookings;
import com.busify.project.promotion.enums.DiscountType;
import com.busify.project.promotion.enums.PromotionStatus;
import com.busify.project.promotion.enums.PromotionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "promotions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotionId;

    @Column(unique = true, nullable = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    @Enumerated(EnumType.STRING)
    private PromotionType promotionType;

    @Column(nullable = false)
    private BigDecimal discountValue;

    private BigDecimal minOrderValue = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = true)
    private Integer usageLimit;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PromotionStatus status = PromotionStatus.active;

    private Integer priority = 0;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private PromotionCampaign campaign;

    @OneToMany(mappedBy = "promotion")
    private List<Bookings> bookings = new ArrayList<>();
}