package com.busify.project.ticket.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
public class UpdateTicketStatusRequestDTO {
    
    @NotEmpty(message = "Danh sách ticket codes không được để trống")
    @Size(max = 50, message = "Không được cập nhật quá 50 vé cùng lúc")
    private List<String> ticketCodes;
    
    @NotBlank(message = "Status không được để trống")
    @Pattern(regexp = "^(used|cancelled)$", message = "Status chỉ được phép là 'used' hoặc 'cancelled'")
    private String status;
    
    @Size(max = 500, message = "Lý do không được quá 500 ký tự")
    private String reason; // Lý do hủy vé (tùy chọn)
}
