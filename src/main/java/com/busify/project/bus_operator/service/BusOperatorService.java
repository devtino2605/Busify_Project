package com.busify.project.bus_operator.service;

import com.busify.project.bus_operator.dto.request.BusOperatorFilterRequest;
import com.busify.project.bus_operator.dto.request.CreateBusOperatorRequest;
import com.busify.project.bus_operator.dto.request.UpdateBusOperatorRequest;
import com.busify.project.bus_operator.dto.response.BusOperatorFilterTripResponse;
import com.busify.project.bus_operator.dto.response.BusOperatorForManagement;
import com.busify.project.bus_operator.dto.response.BusOperatorManagementPageResponse;
import com.busify.project.bus_operator.dto.response.BusOperatorRatingResponse;

import java.util.List;

public interface BusOperatorService {
    List<BusOperatorFilterTripResponse> getAllBusOperators();

    List<BusOperatorRatingResponse> getAllBusOperatorsByRating(Integer limit);

    BusOperatorManagementPageResponse getBusOperatorsForManagement(BusOperatorFilterRequest filterRequest);

    BusOperatorForManagement createBusOperator(CreateBusOperatorRequest request);

    BusOperatorForManagement updateBusOperator(Long id, UpdateBusOperatorRequest request);

    void deleteBusOperator(Long id);

    BusOperatorForManagement getBusOperatorForManagementById(Long id);
}
