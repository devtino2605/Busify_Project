package com.busify.project.ticket.dto.request;

import com.busify.project.ticket.enums.TicketStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TicketUpdateRequestDTO {
    @Size(min = 2, max = 100, message = "Tên hành khách phải từ 2-100 ký tự")
    @Pattern(regexp = "^[\\p{L}\\s]{2,100}$", message = "Tên chỉ được chứa chữ cái và khoảng trắng")
    private String passengerName;
    
    @Pattern(regexp = "^(\\+84|0)(3|5|7|8|9)[0-9]{8}$", message = "Số điện thoại không hợp lệ")
    private String passengerPhone;
    
    @Email(message = "Email không hợp lệ")
    @Size(max = 100, message = "Email không được quá 100 ký tự")
    private String email;
    
    @Pattern(regexp = "^[A-Z][0-9]{1,2}$", message = "Số ghế phải có định dạng như A1, B12")
    private String seatNumber;
    
    private TicketStatus status;
}
