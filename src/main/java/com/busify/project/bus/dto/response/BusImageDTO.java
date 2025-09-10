package com.busify.project.bus.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusImageDTO {
    private Long id;
    private String imageUrl;
    private boolean primary;
}

