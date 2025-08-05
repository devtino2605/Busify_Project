package com.busify.project.bus_operator.service;

import com.busify.project.bus_operator.dto.response.BusOperatorFilterTripResponse;
import com.busify.project.bus_operator.dto.response.BusOperatorRatingResponse;

import java.util.List;

public interface BusOperatorService {
    List<BusOperatorFilterTripResponse> getAllBusOperators();
    List<BusOperatorRatingResponse> getAllBusOperatorsByRating(Integer limit);
}
