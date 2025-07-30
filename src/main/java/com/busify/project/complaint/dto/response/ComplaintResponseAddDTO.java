package com.busify.project.complaint.dto.response;

import com.busify.project.complaint.enums.ComplaintStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ComplaintResponseAddDTO extends ComplaintResponseDTO {
    
    private ComplaintStatus status;
    private String title;
    private String description;
    private Long assignedAgentId;
}
