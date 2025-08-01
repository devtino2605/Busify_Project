package com.busify.project.employee.entity;

import com.busify.project.bus_operator.entity.BusOperator;
import com.busify.project.employee.enums.EmployeeStatus;
import com.busify.project.employee.enums.EmployeeType;
import com.busify.project.user.entity.Profile;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "employees")
public class Employee extends Profile {
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "operator_id")
    private BusOperator operator;

    @Column(name = "employee_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeeType employeeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EmployeeStatus status;

    @Column(name = "driver_license_number")
    private String driverLicenseNumber;
}