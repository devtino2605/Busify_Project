package com.busify.project.booking_promotion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingPromotionId implements Serializable {
    @Column(name = "booking_id")
    private Long bookingId;

    @Column(name = "promotion_id")
    private Long promotionId;
}