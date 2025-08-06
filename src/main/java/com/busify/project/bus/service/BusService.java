package com.busify.project.bus.service;

import org.springframework.stereotype.Service;

import com.busify.project.bus.dto.response.BusLayoutResponseDTO;
import com.busify.project.bus.repository.BusRepository;
import com.busify.project.seat_layout.repository.SeatLayoutRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusService {
    private final BusRepository busRepository;

    private final SeatLayoutRepository seatLayoutRepository;

    private final ObjectMapper objectMapper;

    /**
     * Retrieves the bus seat layout map for a given bus ID.
     *
     * @param busId the ID of the bus
     * @return a BusLayoutResponseDTO containing the layout information
     *         @implNote
     *         There is no need to use complex data structures like Map or List
     *         here,
     *         as the layout just contains rows, columns, and floors.
     *         Consider change layout data to
     *         {
     *         "rows": 10,
     *         "cols": 4,
     *         "floors": 1
     *         }
     */
    public BusLayoutResponseDTO getBusSeatLayoutMap(Long busId) {
        return busRepository.findById(busId)
                .map(bus -> {
                    if (bus.getSeatLayout() == null) {
                        throw new IllegalArgumentException("Seat layout is null for bus ID: " + busId);
                    }
                    return seatLayoutRepository.findById(bus.getSeatLayout().getId())
                            .map(seatLayout -> {
                                JsonNode layout = objectMapper.convertValue(seatLayout.getLayoutData(), JsonNode.class);
                                int rows = layout.get("rows").asInt();
                                int columns = layout.get("cols").asInt();
                                int floors = layout.has("floors") ? layout.get("floors").asInt() : 1;

                                return new BusLayoutResponseDTO(rows, columns, floors);
                            })
                            .orElseThrow(
                                    () -> new IllegalArgumentException("Seat layout not found for bus ID: " + busId));
                })
                .orElseThrow(() -> new IllegalArgumentException("Bus not found with ID: " + busId));
    }
}
