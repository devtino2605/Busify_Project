package com.busify.project.location.repository;

import com.busify.project.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    
    /**
     * Tìm kiếm locations theo name hoặc city (case insensitive)
     */
    @Query("SELECT l FROM Location l WHERE " +
           "LOWER(l.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.city) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Location> searchByNameOrCity(@Param("keyword") String keyword);
    
    /**
     * Lấy tất cả locations sắp xếp theo city và name
     */
    @Query("SELECT l FROM Location l ORDER BY l.city ASC, l.name ASC")
    List<Location> findAllOrderByCityAndName();
}
