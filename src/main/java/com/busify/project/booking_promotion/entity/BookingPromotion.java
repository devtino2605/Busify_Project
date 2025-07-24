package com.busify.project.booking_promotion.entity;


import com.busify.project.booking.entity.Bookings;
import com.busify.project.promotion.entity.Promotion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "booking_promotions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingPromotion {
    @EmbeddedId
    private BookingPromotionId id;

    @ManyToOne
    @MapsId("bookingId")
    @JoinColumn(name = "booking_id")
    private Bookings booking;

    @ManyToOne
    @MapsId("promotionId")
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;
}
