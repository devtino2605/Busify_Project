package com.busify.project.complaint.dto;

import com.busify.project.complaint.enums.ComplaintStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintUpdateDTO {
    private String title;
    private String description;
    private ComplaintStatus status;
    private Long assignedAgentId;
}
