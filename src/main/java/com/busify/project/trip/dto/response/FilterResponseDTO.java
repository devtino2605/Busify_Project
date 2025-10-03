package com.busify.project.trip.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterResponseDTO {
    private int page;
    private int size;
    private int totalPages;
    private boolean isFirst;
    private boolean isLast;
    private List<TripFilterResponseDTO> data;
}
