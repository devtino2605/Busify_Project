// Controller
package com.busify.project.bus_operator.controller;

import com.busify.project.bus_operator.dto.response.BusOperatorRatingResponse;
import com.busify.project.bus_operator.service.BusOperatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bus-operators")
@RequiredArgsConstructor
public class BusOperatorController {

    private final BusOperatorService busOperatorService;

    @GetMapping("/rating")
    public ResponseEntity<List<BusOperatorRatingResponse>> getAllBusOperatorsByRatingWithLimit(
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        List<BusOperatorRatingResponse> busOperators = busOperatorService.getAllBusOperatorsByRating(limit);
        return ResponseEntity.ok(busOperators);
    }

}