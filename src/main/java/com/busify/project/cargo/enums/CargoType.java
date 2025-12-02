package com.busify.project.cargo.enums;

/**
 * CargoType Enum
 * 
 * Represents different types of cargo that can be shipped
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
public enum CargoType {
    /**
     * Documents, papers, letters
     */
    DOCUMENT("Tài liệu", 0.5, 50000),

    /**
     * General packages and parcels
     */
    PACKAGE("Hàng hóa thường", 1.0, 100000),

    /**
     * Fragile items requiring special handling
     */
    FRAGILE("Hàng dễ vỡ", 1.5, 150000),

    /**
     * Electronic devices and equipment
     */
    ELECTRONICS("Thiết bị điện tử", 1.3, 120000),

    /**
     * Other types not covered above
     */
    OTHER("Khác", 1.0, 100000);

    private final String displayName;
    private final double multiplier; // Fee multiplier based on cargo type
    private final int baseInsuranceFee; // Base insurance fee in VND

    CargoType(String displayName, double multiplier, int baseInsuranceFee) {
        this.displayName = displayName;
        this.multiplier = multiplier;
        this.baseInsuranceFee = baseInsuranceFee;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getMultiplier() {
        return multiplier;
    }

    /**
     * Alias for getMultiplier() - used in service layer
     */
    public double getFeeMultiplier() {
        return multiplier;
    }

    public int getBaseInsuranceFee() {
        return baseInsuranceFee;
    }

    /**
     * Check if this cargo type requires special handling
     */
    public boolean requiresSpecialHandling() {
        return this == FRAGILE || this == ELECTRONICS;
    }

    /**
     * Check if insurance is recommended for this cargo type
     */
    public boolean isInsuranceRecommended() {
        return this == FRAGILE || this == ELECTRONICS;
    }
}
