package com.busify.project.bus_operator.service.imp;

import com.busify.project.bus_operator.dto.response.BusOperatorFilterTripResponse;
import com.busify.project.bus_operator.dto.response.BusOperatorRatingResponse;
import com.busify.project.bus_operator.mapper.BusOperatorMapper;
import com.busify.project.bus_operator.repository.BusOperatorRepository;
import com.busify.project.bus_operator.service.BusOperatorService;
import com.busify.project.route.dto.response.RouteFilterTripResponse;
import com.busify.project.route.mapper.RouteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusOperatorServiceImpl implements BusOperatorService {

    private final BusOperatorRepository busOperatorRepository;

    @Override
    public List<BusOperatorFilterTripResponse> getAllBusOperators() {
        return busOperatorRepository.findAll()
                .stream()
                .map(BusOperatorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BusOperatorRatingResponse> getAllBusOperatorsByRating(Integer limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return busOperatorRepository.findAllOperatorsWithRatings(pageable);
    }
}
