package com.busify.project.complaint.dto.response;

import com.busify.project.complaint.enums.ComplaintStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ComplaintResponseGetAllDTO extends ComplaintResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String customerName;
    private ComplaintStatus status;
    private Long bookingId;
    private Long assignedAgentId;
}
