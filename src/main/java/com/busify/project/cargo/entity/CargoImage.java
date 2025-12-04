package com.busify.project.cargo.entity;

import com.busify.project.cargo.enums.ImageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * CargoImage Entity
 * 
 * Represents images associated with a cargo booking
 * (photos of package, delivery proof, damage reports, etc.)
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
@Entity
@Table(name = "cargo_images", indexes = {
        @Index(name = "idx_cargo_image_booking", columnList = "cargo_booking_id"),
        @Index(name = "idx_cargo_image_type", columnList = "image_type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CargoImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_booking_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private CargoBooking cargoBooking;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl; // URL to the uploaded image (Cloudinary, S3, etc.)

    @Enumerated(EnumType.STRING)
    @Column(name = "image_type", nullable = false)
    private ImageType imageType;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // ===== CONSTRUCTOR FOR EASY CREATION =====

    public CargoImage(CargoBooking cargoBooking, String imageUrl, ImageType imageType) {
        this.cargoBooking = cargoBooking;
        this.imageUrl = imageUrl;
        this.imageType = imageType;
        this.uploadedAt = LocalDateTime.now();
    }

    public CargoImage(CargoBooking cargoBooking, String imageUrl, ImageType imageType, String description) {
        this.cargoBooking = cargoBooking;
        this.imageUrl = imageUrl;
        this.imageType = imageType;
        this.description = description;
        this.uploadedAt = LocalDateTime.now();
    }
}
