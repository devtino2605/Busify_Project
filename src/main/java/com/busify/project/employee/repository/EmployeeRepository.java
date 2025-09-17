package com.busify.project.employee.repository;

import com.busify.project.employee.dto.response.EmployeeForOperatorResponse;
import com.busify.project.employee.entity.Employee;
import com.busify.project.user.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Query(value = """
            SELECT 
                e.id,
                p.full_name,
                u.email,
                p.phone_number,
                e.employee_type,
                p.status,
                e.driver_license_number,
                bo.operator_id as operatorId,
                bo.name as operatorName
            FROM employees e
            INNER JOIN profiles p ON e.id = p.id
            INNER JOIN users u ON e.id = u.id
            LEFT JOIN bus_operators bo ON e.operator_id = bo.operator_id
            WHERE e.employee_type = 'DRIVER'
            """, nativeQuery = true)
    List<Object[]> findAllDrivers();

    @Query(value = """
            SELECT 
                e.id,
                p.full_name,
                u.email,
                p.phone_number,
                e.employee_type,
                p.status,
                e.driver_license_number,
                bo.operator_id as operatorId,
                bo.name as operatorName
            FROM employees e
            INNER JOIN profiles p ON e.id = p.id
            INNER JOIN users u ON e.id = u.id
            LEFT JOIN bus_operators bo ON e.operator_id = bo.operator_id
            """, nativeQuery = true)
    List<Object[]> findAllEmployees();

    @Query("SELECT new com.busify.project.employee.dto.response.EmployeeForOperatorResponse(e.id, e.fullName) " +
            "FROM Employee e WHERE e.operator.id = :operatorId")
    List<EmployeeForOperatorResponse> findDriversByOperator(@Param("operatorId") Long operatorId);

    @Query("""
                SELECT e.operator.id
                FROM Employee e
                WHERE e.id = :userId
            """)
    Optional<Long> findOperatorIdByStaffUserId(@Param("userId") Long userId);

}
