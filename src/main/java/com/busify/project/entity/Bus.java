package com.busify.project.entity;

import com.busify.project.enums.BusStatus;
import com.busify.project.enums.EmployeeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "buses", indexes = {
        @Index(name = "idx_buses_licensePlate", columnList = "license_plate")
})
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "operator_id")
    private BusOperator operator;

    @Column(name = "license_plate", nullable = false, length = 50)
    private String licensePlate;

    @Column(name = "model")
    private String model;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "seat_layout_id")
    private SeatLayout seatLayout;

    @Column(name = "amenities")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> amenities;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BusStatus status = BusStatus.ACTIVE;
}