package com.busify.project.promotion.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean deleted;

    @OneToMany(mappedBy = "campaign")
    private List<Promotion> promotions = new ArrayList<>();
}
