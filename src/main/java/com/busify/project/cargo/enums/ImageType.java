package com.busify.project.cargo.enums;

/**
 * ImageType Enum
 * 
 * Represents different types of images that can be associated with a cargo
 * booking
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
public enum ImageType {
    /**
     * Photo of the package/cargo itself
     * Used when: Sender/staff takes photo of the cargo before shipment
     */
    PACKAGE("Hình ảnh hàng hóa"),

    /**
     * Photo of shipping label or barcode
     * Used when: Staff attaches label to cargo and needs proof
     */
    LABEL("Nhãn vận chuyển"),

    /**
     * Proof of delivery (signature, photo with receiver)
     * Used when: Driver/staff confirms delivery with photo evidence
     */
    DELIVERY_PROOF("Chứng nhận giao hàng"),

    /**
     * Photo showing damage to cargo
     * Used when: Cargo is damaged during transport, need evidence for claims
     */
    DAMAGE("Hình ảnh hư hại"),

    /**
     * Other types of images
     * Used when: Any other relevant photos not covered by above categories
     */
    OTHER("Khác");

    private final String displayName;

    ImageType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Get Vietnamese display name for the image type
     * 
     * @return Display name in Vietnamese
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Check if this image type is required for cargo booking
     * 
     * @return true if this image type must be uploaded
     */
    public boolean isRequired() {
        return this == PACKAGE; // Only PACKAGE photo is mandatory
    }

    /**
     * Check if this image type is for delivery confirmation
     * 
     * @return true if this is delivery proof
     */
    public boolean isDeliveryProof() {
        return this == DELIVERY_PROOF;
    }

    /**
     * Check if this image type is for damage reporting
     * 
     * @return true if this is damage photo
     */
    public boolean isDamageProof() {
        return this == DAMAGE;
    }
}
