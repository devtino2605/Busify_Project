package com.busify.project.cargo.repository;

import com.busify.project.cargo.entity.CargoImage;
import com.busify.project.cargo.enums.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CargoImageRepository
 * 
 * Repository interface for CargoImage entity
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
@Repository
public interface CargoImageRepository extends JpaRepository<CargoImage, Long> {

    /**
     * Find all images for a cargo booking
     */
    List<CargoImage> findByCargoBookingCargoBookingId(Long cargoBookingId);

    /**
     * Find images by cargo booking and image type
     */
    List<CargoImage> findByCargoBookingCargoBookingIdAndImageType(Long cargoBookingId, ImageType imageType);

    /**
     * Find images by image type
     */
    List<CargoImage> findByImageType(ImageType imageType);

    /**
     * Count images for a cargo booking
     */
    long countByCargoBookingCargoBookingId(Long cargoBookingId);

    /**
     * Delete all images for a cargo booking
     */
    void deleteByCargoBookingCargoBookingId(Long cargoBookingId);
}
