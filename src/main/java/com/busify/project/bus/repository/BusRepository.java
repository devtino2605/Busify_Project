package com.busify.project.bus.repository;

import com.busify.project.bus.entity.Bus;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.busify.project.bus_operator.entity.BusOperator;


@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    List<Bus> findByOperator(BusOperator operator);

    @Query(value = """
    SELECT b.* FROM buses b
    JOIN bus_models bm ON b.model_id = bm.id
    WHERE (:keyword IS NULL OR :keyword = '' 
           OR LOWER(b.license_plate) LIKE LOWER(CONCAT('%', :keyword, '%')) 
           OR LOWER(bm.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
      AND (:status IS NULL OR b.status = :status)
      AND (
          :#{#amenities == null || #amenities.isEmpty()} = true
          OR NOT EXISTS (
              SELECT 1
              FROM JSON_TABLE(:amenitiesJson, '$[*]' COLUMNS(amenity VARCHAR(50) PATH '$')) a
              WHERE JSON_EXTRACT(b.amenities, CONCAT('$.', a.amenity)) <> true
          )
      )
    """,
            countQuery = """
    SELECT COUNT(*) FROM buses b
    JOIN bus_models bm ON b.model_id = bm.id
    WHERE (:keyword IS NULL OR :keyword = '' 
           OR LOWER(b.license_plate) LIKE LOWER(CONCAT('%', :keyword, '%')) 
           OR LOWER(bm.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
      AND (:status IS NULL OR b.status = :status)
      AND (
          :#{#amenities == null || #amenities.isEmpty()} = true
          OR NOT EXISTS (
              SELECT 1
              FROM JSON_TABLE(:amenitiesJson, '$[*]' COLUMNS(amenity VARCHAR(50) PATH '$')) a
              WHERE JSON_EXTRACT(b.amenities, CONCAT('$.', a.amenity)) <> true
          )
      )
    """,
            nativeQuery = true)
    Page<Bus> searchAndFilterBuses(
            @Param("keyword") String keyword,
            @Param("status") String status,
            @Param("amenities") List<String> amenities,
            @Param("amenitiesJson") String amenitiesJson,
            Pageable pageable
    );
}
