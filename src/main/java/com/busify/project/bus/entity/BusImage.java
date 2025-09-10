package com.busify.project.bus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bus_images")
public class BusImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết tới Bus (nhiều ảnh thuộc 1 bus)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;

    // URL ảnh (lưu đường dẫn Cloudinary hoặc S3)
    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "public_id", nullable = false, length = 500)
    private String publicId;

    // Đánh dấu ảnh chính (thumbnail / avatar)
    @Column(name = "is_primary", nullable = false)
    private boolean primary = false;
}
