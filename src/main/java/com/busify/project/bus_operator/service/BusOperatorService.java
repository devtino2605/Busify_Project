package com.busify.project.bus_operator.service;

import com.busify.project.bus_operator.dto.response.BusOperatorRatingResponse;

import java.util.List;

public interface BusOperatorService {

    List<BusOperatorRatingResponse> getAllBusOperatorsByRating(Integer limit);
}
