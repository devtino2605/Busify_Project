package com.busify.project.trip.dto.request;

import com.busify.project.trip.enums.TripStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripMGMTRequestDTO {

    @NotNull(message = "Route ID không được để trống")
    private Long routeId;

    @NotNull(message = "Bus ID không được để trống")
    private Long busId;

    @NotNull(message = "Driver ID không được để trống")
    private Long driverId;

    @NotNull(message = "Thời gian khởi hành không được để trống")
//    @Future(message = "Thời gian khởi hành phải ở tương lai")
    private Instant departureTime;

    @NotNull(message = "Giá vé không được để trống")
    @DecimalMin(value = "0.01", inclusive = true, message = "Giá vé phải lớn hơn 0")
    private BigDecimal pricePerSeat;

    @NotNull(message = "Trạng thái chuyến đi không được để trống")
    private TripStatus status;
}
