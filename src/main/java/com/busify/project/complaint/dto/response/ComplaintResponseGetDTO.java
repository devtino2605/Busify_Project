package com.busify.project.complaint.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ComplaintResponseGetDTO extends ComplaintResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String customerName;
    private String createdAt;
}
