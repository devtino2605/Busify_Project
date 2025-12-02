package com.busify.project.bus_model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "bus_models")
public class BusModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Maximum cargo weight capacity for this bus model (in kg)
     * This varies based on bus size and design:
     * - Small buses (16 seats): ~200 kg
     * - Medium buses (29 seats): ~350 kg
     * - Large buses (45+ seats): ~500 kg
     * 
     * Default: 500.0 kg if not specified
     */
    @Column(name = "max_cargo_weight", precision = 10, scale = 2)
    private BigDecimal maxCargoWeight;

    /**
     * Get max cargo weight with default value
     * 
     * @return max cargo weight (default 500.0 kg if null)
     */
    public BigDecimal getMaxCargoWeightOrDefault() {
        return maxCargoWeight != null ? maxCargoWeight : new BigDecimal("500.0");
    }
}
