package com.busify.project.route.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteStopDTO {
    private Long locationId;
    private String locationName;
    private String locationCity;
    private String locationAddress;
    private Integer stopOrder;
    private Integer timeOffsetFromStart; // Thời gian từ điểm bắt đầu (phút)
}