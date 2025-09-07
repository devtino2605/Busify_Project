package com.busify.project.bus.repository;

import com.busify.project.bus.entity.BusImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusImageRepository extends JpaRepository<BusImage, Long> {
    List<BusImage> findByBusId(Long busId);
    Optional<BusImage> findByBusIdAndImageUrl(Long busId, String imageUrl);
}