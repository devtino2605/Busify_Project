package com.busify.project.tripSeat;

import com.busify.project.trip_seat.dto.SeatStatus;
import com.busify.project.trip_seat.entity.TripSeat;
import com.busify.project.trip_seat.entity.TripSeatId;
import com.busify.project.trip_seat.enums.TripSeatStatus;
import com.busify.project.trip_seat.repository.TripSeatRepository;
import com.busify.project.trip_seat.services.TripSeatService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class TripSeatServiceTest {

    @Mock
    private TripSeatRepository tripSeatRepository;

    @InjectMocks
    private TripSeatService tripSeatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTripSeatsStatus() {
        Long tripId = 1L;

        // Giả lập dữ liệu từ repository
        TripSeat seat1 = new TripSeat();
        seat1.setId(new TripSeatId(tripId, "A.1.1"));
        seat1.setStatus(TripSeatStatus.valueOf("available"));

        TripSeat seat2 = new TripSeat();
        seat2.setId(new TripSeatId(tripId, "A.1.2"));
        seat2.setStatus(TripSeatStatus.valueOf("booked"));

        when(tripSeatRepository.findByTripId(tripId))
                .thenReturn(Arrays.asList(seat1, seat2));

        // Gọi service
        List<SeatStatus> result = tripSeatService.getTripSeatsStatus(tripId);

        // Kiểm tra kết quả
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getSeatNumber()).isEqualTo("A.1.1");
        assertThat(result.get(0).getStatus()).isEqualTo("available");
        assertThat(result.get(1).getSeatNumber()).isEqualTo("A.1.2");
        assertThat(result.get(1).getStatus()).isEqualTo("booked");
    }
}
