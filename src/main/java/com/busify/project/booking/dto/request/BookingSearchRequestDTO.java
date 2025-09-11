package com.busify.project.booking.dto.request;

import com.busify.project.booking.enums.BookingStatus;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingSearchRequestDTO {
    
    @Size(min = 6, max = 20, message = "Mã booking phải từ 6-20 ký tự")
    @Pattern(regexp = "^[A-Z0-9]*$", message = "Mã booking chỉ được chứa chữ hoa và số")
    private String bookingCode;
    
    @Size(min = 2, max = 100, message = "Tên khách hàng phải từ 2-100 ký tự")
    private String customerName;
    
    @Pattern(regexp = "^(\\+84|0)(3|5|7|8|9)[0-9]{8}$", message = "Số điện thoại không hợp lệ")
    private String customerPhone;
    
    private BookingStatus status;
    
    private LocalDate fromDate;
    private LocalDate toDate;
    
    private Long tripId;
}