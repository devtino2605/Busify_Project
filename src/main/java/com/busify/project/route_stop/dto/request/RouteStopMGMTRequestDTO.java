package com.busify.project.route_stop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteStopMGMTRequestDTO {

    @NotNull(message = "Route ID không được để trống")
    private Long routeId;

    @NotNull(message = "Location ID không được để trống")
    private Long locationId;

    @NotNull(message = "Thứ tự điểm dừng không được để trống")
    @Min(value = 1, message = "Thứ tự điểm dừng phải >= 1")
    private Integer stopOrder;

    @NotNull(message = "Thời gian lệch so với điểm bắt đầu không được để trống")
    @Min(value = 0, message = "Thời gian lệch phải >= 0 phút")
    private Integer timeOffsetFromStart;
}
