package com.busify.project.bus_operator.dto.request;

import com.busify.project.bus_operator.enums.OperatorStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusOperatorFilterRequest {
    private String search; // Search in name, email, hotline
    private OperatorStatus status; // Filter by operator status
    private String ownerName; // Filter by owner's name
    @Builder.Default
    private int page = 0; // Page number (0-based)
    @Builder.Default
    private int size = 10; // Page size
    @Builder.Default
    private String sortBy = "operatorName"; // Sort field
    @Builder.Default
    private String sortDirection = "asc"; // Sort direction (asc/desc)
}
