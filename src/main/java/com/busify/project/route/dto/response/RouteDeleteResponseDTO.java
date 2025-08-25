package com.busify.project.route.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDeleteResponseDTO {
    private Long id;
    private String name;
}
