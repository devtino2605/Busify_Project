package com.busify.project.promotion.entity;


import com.busify.project.booking.entity.Bookings;
import com.busify.project.promotion.enums.DiscountType;
import com.busify.project.promotion.enums.PromotionStatus;
import com.busify.project.user.entity.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "promotions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotionId;

    @Column(unique = true, nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    @Column(nullable = false)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer usageLimit = 0;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PromotionStatus status = PromotionStatus.active;

    @ManyToMany
    @JoinTable(
            name = "promotion_user",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Profile> profiles = new HashSet<>();

    @OneToMany(mappedBy = "promotion")
    private List<Bookings> bookings = new ArrayList<>();
}