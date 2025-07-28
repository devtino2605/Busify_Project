package com.busify.project.complaint.dto.response;

import java.util.List;

import com.busify.project.complaint.dto.ComplaintAddDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintResponseListDTO extends ComplaintResponseDTO {
    private List<ComplaintAddDTO> complaints;
}
