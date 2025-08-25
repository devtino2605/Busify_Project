package com.busify.project.tripSeat;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.trip_seat.controller.TripSeatController;
import com.busify.project.trip_seat.dto.SeatStatus;
import com.busify.project.trip_seat.dto.TripSeatsStatusReponse;
import com.busify.project.trip_seat.enums.TripSeatStatus;
import com.busify.project.trip_seat.services.TripSeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class TripSeatControllerTest {

    @Mock
    private TripSeatService tripSeatService;

    @InjectMocks
    private TripSeatController tripSeatController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSeatAvailability() {
        // Giả lập dữ liệu giả cho service
        Long tripId = 1L;
        List<SeatStatus> mockSeatStatus = Arrays.asList( new SeatStatus("A.1.1", TripSeatStatus.available),
    new SeatStatus("A.1.2", TripSeatStatus.booked));

        when(tripSeatService.getTripSeatsStatus(tripId)).thenReturn(mockSeatStatus);

        // Gọi controller
        ResponseEntity<ApiResponse<TripSeatsStatusReponse>> responseEntity =
                tripSeatController.getSeatAvailability(tripId);

        // Kiểm tra HTTP status
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);

        // Lấy body và kiểm tra dữ liệu
        ApiResponse<TripSeatsStatusReponse> body = responseEntity.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getMessage()).isEqualTo("Trip seats status fetched successfully");
        assertThat(body.getResult().getTripId()).isEqualTo(tripId);
        assertThat(body.getResult().getSeatsStatus()).isEqualTo(mockSeatStatus);
    }
}
