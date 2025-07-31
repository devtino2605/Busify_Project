package com.busify.project.complaint.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class ComplaintResponseGetDTO extends ComplaintResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String customerName;
    private String createdAt;
}
