package com.busify.project.ticket.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.List;

@Data
public class UpdateTicketStatusRequestDTO {
    
    @NotEmpty(message = "Danh sách ticket codes không được để trống")
    private List<String> ticketCodes;
    
    @NotBlank(message = "Status không được để trống")
    @Pattern(regexp = "^(used|cancelled)$", message = "Status chỉ được phép là 'used' hoặc 'cancelled'")
    private String status;
    
    private String reason; // Lý do hủy vé (tùy chọn)
}
