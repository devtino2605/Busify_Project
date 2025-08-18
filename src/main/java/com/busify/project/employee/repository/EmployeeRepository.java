package com.busify.project.employee.repository;

import com.busify.project.employee.entity.Employee;
import com.busify.project.employee.enums.EmployeeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
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
            INNER JOIN users u ON p.user_id = u.id
            LEFT JOIN bus_operators bo ON e.operator_id = bo.operator_id
            WHERE e.employee_type = 'DRIVER'
            """, nativeQuery = true)
    List<Object[]> findAllDrivers();
}
