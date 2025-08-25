package com.busify.project.employee.entity;

import com.busify.project.bus_operator.entity.BusOperator;
import com.busify.project.user.entity.Profile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@Table(name = "employees")
public class Employee extends Profile {
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "operator_id")
    private BusOperator operator;

    @Column(name = "driver_license_number")
    private String driverLicenseNumber;
}