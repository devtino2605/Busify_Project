package com.busify.project.cargo.dto.request;

import com.busify.project.cargo.enums.CargoType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * CargoBookingRequestDTO
 * 
 * Request DTO for creating a cargo booking
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CargoBookingRequestDTO {

    @NotNull(message = "Trip ID không được để trống")
    @Positive(message = "Trip ID phải là số dương")
    private Long tripId;

    // ===== SENDER INFORMATION =====
    // Optional if bookingUserId is provided, will auto-fill from user profile
    // But can be overridden

    @Size(max = 100, message = "Tên người gửi không được vượt quá 100 ký tự")
    private String senderName;

    @Pattern(regexp = "^(\\+84|0)[0-9]{9}$", message = "Số điện thoại không hợp lệ")
    private String senderPhone;

    @Email(message = "Email người gửi không hợp lệ")
    private String senderEmail;

    private String senderAddress;

    // ===== RECEIVER INFORMATION =====
    // Always required

    @NotBlank(message = "Tên người nhận không được để trống")
    @Size(max = 100, message = "Tên người nhận không được vượt quá 100 ký tự")
    private String receiverName;

    @NotBlank(message = "Số điện thoại người nhận không được để trống")
    @Pattern(regexp = "^(\\+84|0)[0-9]{9}$", message = "Số điện thoại người nhận không hợp lệ")
    private String receiverPhone;

    @Email(message = "Email người nhận không hợp lệ")
    private String receiverEmail;

    private String receiverAddress;

    // ===== CARGO INFORMATION =====

    @NotNull(message = "Loại hàng không được để trống")
    private CargoType cargoType;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String description;

    @NotNull(message = "Cân nặng không được để trống")
    @DecimalMin(value = "0.1", message = "Cân nặng tối thiểu 0.1 kg")
    @DecimalMax(value = "50.0", message = "Cân nặng tối đa 50 kg")
    @Digits(integer = 8, fraction = 2, message = "Cân nặng không hợp lệ")
    private BigDecimal weight;

    @Pattern(regexp = "^\\d+x\\d+x\\d+$", message = "Kích thước phải theo định dạng DxRxC (ví dụ: 30x20x10)")
    private String dimensions; // Format: LxWxH cm

    @DecimalMin(value = "0", message = "Giá trị khai báo phải >= 0")
    @Digits(integer = 10, fraction = 2, message = "Giá trị khai báo không hợp lệ")
    private BigDecimal declaredValue;

    // ===== PICKUP/DROPOFF LOCATIONS =====

    @NotNull(message = "Điểm lấy hàng không được để trống")
    @Positive(message = "Điểm lấy hàng không hợp lệ")
    private Long pickupLocationId;

    @NotNull(message = "Điểm giao hàng không được để trống")
    @Positive(message = "Điểm giao hàng không hợp lệ")
    private Long dropoffLocationId;

    @Size(max = 500, message = "Ghi chú đón hàng không được vượt quá 500 ký tự")
    private String pickupNotes;

    @Size(max = 500, message = "Ghi chú giao hàng không được vượt quá 500 ký tự")
    private String dropoffNotes;

    // ===== SPECIAL INSTRUCTIONS =====

    @Size(max = 1000, message = "Hướng dẫn đặc biệt không được vượt quá 1000 ký tự")
    private String specialInstructions;

    // ===== INSURANCE =====

    private Boolean needsInsurance = false;

}
