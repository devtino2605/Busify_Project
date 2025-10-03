package com.busify.project.booking.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingUpdateRequestDTO {
    @Size(min = 2, max = 100, message = "Tên khách hàng phải từ 2-100 ký tự")
    @Pattern(regexp = "^[\\p{L}\\s]{2,100}$", message = "Tên chỉ được chứa chữ cái và khoảng trắng")
    private String guestFullName;
    
    @Pattern(regexp = "^(\\+84|0)(3|5|7|8|9)[0-9]{8}$", message = "Số điện thoại không hợp lệ")
    private String guestPhone;
    
    @Email(message = "Email không hợp lệ")
    @Size(max = 100, message = "Email không được quá 100 ký tự")
    private String guestEmail;
    
    @Size(max = 200, message = "Địa chỉ không được quá 200 ký tự")
    private String guestAddress;
}
