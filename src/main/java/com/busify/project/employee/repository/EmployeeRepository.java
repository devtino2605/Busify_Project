package com.busify.project.employee.repository;

import com.busify.project.employee.entity.Employee;
import com.busify.project.user.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
        SELECT e FROM Employee e
        WHERE (:keyword IS NULL OR :keyword = '' 
               OR LOWER(e.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:status IS NULL OR e.status = :status)
          AND (:operatorId IS NULL OR e.operator.id = :operatorId)
    """)
    Page<Employee> searchEmployees(
            @Param("keyword") String keyword,
            @Param("status") UserStatus status,
            @Param("operatorId") Long operatorId,
            Pageable pageable
    );
}
