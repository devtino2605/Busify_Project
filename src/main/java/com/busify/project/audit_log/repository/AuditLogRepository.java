package com.busify.project.audit_log.repository;

import com.busify.project.audit_log.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    
    // Find all audit logs ordered by timestamp descending with pagination
    Page<AuditLog> findAllByOrderByTimestampDesc(Pageable pageable);
    
    // Find audit logs by user with pagination
    @Query("SELECT a FROM AuditLog a WHERE a.user.id = :userId ORDER BY a.timestamp DESC")
    Page<AuditLog> findByUserIdOrderByTimestampDesc(@Param("userId") Long userId, Pageable pageable);
    
    // Find audit logs by action with pagination
    Page<AuditLog> findByActionOrderByTimestampDesc(String action, Pageable pageable);
    
    // Find audit logs by target entity with pagination
    Page<AuditLog> findByTargetEntityOrderByTimestampDesc(String targetEntity, Pageable pageable);
    
    // Find audit logs within date range with pagination
    @Query("SELECT a FROM AuditLog a WHERE a.timestamp BETWEEN :startDate AND :endDate ORDER BY a.timestamp DESC")
    Page<AuditLog> findByTimestampBetweenOrderByTimestampDesc(
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate, 
        Pageable pageable
    );
    
    // Find audit logs by multiple filters with pagination
    @Query("SELECT a FROM AuditLog a WHERE " +
           "(:userId IS NULL OR a.user.id = :userId) AND " +
           "(:action IS NULL OR a.action = :action) AND " +
           "(:targetEntity IS NULL OR a.targetEntity = :targetEntity) AND " +
           "(:startDate IS NULL OR a.timestamp >= :startDate) AND " +
           "(:endDate IS NULL OR a.timestamp <= :endDate) " +
           "ORDER BY a.timestamp DESC")
    Page<AuditLog> findByFiltersOrderByTimestampDesc(
        @Param("userId") Long userId,
        @Param("action") String action,
        @Param("targetEntity") String targetEntity,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );
}