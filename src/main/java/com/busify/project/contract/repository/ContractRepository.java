package com.busify.project.contract.repository;

import com.busify.project.contract.entity.Contract;
import com.busify.project.contract.enums.ContractStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    // Tìm tất cả hợp đồng theo email của bus operator
    List<Contract> findByEmailOrderByCreatedDateDesc(String email);
    
    // Tìm hợp đồng theo trạng thái
    Page<Contract> findByStatusOrderByCreatedDateDesc(ContractStatus status, Pageable pageable);
    
    // Tìm tất cả hợp đồng với phân trang
    Page<Contract> findAllByOrderByCreatedDateDesc(Pageable pageable);
    
    // Đếm số lượng hợp đồng theo trạng thái
    long countByStatus(ContractStatus status);
    
    // Tìm kiếm hợp đồng theo nhiều tiêu chí
    @Query("SELECT c FROM Contract c WHERE " +
           "(:email IS NULL OR c.email LIKE %:email%) AND " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:operationArea IS NULL OR c.operationArea LIKE %:operationArea%) " +
           "ORDER BY c.createdDate DESC")
    Page<Contract> findContractsWithFilters(@Param("email") String email,
                                          @Param("status") ContractStatus status,
                                          @Param("operationArea") String operationArea,
                                          Pageable pageable);
}