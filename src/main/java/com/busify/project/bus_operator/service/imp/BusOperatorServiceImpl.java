package com.busify.project.bus_operator.service.imp;

import com.busify.project.bus_operator.dto.response.BusOperatorDetailsResponse;
import com.busify.project.bus_operator.dto.response.BusOperatorFilterTripResponse;
import com.busify.project.bus_operator.dto.response.BusOperatorRatingResponse;
import com.busify.project.bus_operator.dto.response.BusOperatorResponse;
import com.busify.project.bus_operator.entity.BusOperator;
import com.busify.project.bus_operator.enums.OperatorStatus;
import com.busify.project.bus_operator.mapper.BusOperatorMapper;
import com.busify.project.bus_operator.repository.BusOperatorRepository;
import com.busify.project.bus_operator.service.BusOperatorService;
import com.busify.project.review.repository.ReviewRepository;

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

    private final ReviewRepository reviewRepository;

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

    public BusOperatorDetailsResponse getOperatorById(Long id) {
        final BusOperator busOperator = busOperatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bus operator not found with id: " + id));
        final Double rating = reviewRepository.findAverageRatingByOperatorId(id);
        final Long totalReviews = reviewRepository.countByBusOperatorId(id);
        return new BusOperatorDetailsResponse(
                busOperator.getId(),
                busOperator.getName(),
                busOperator.getEmail(),
                busOperator.getHotline(),
                busOperator.getDescription(),
                "/logo.png",
                busOperator.getAddress(),
                rating != null ? rating : 0.0,
                totalReviews != null ? totalReviews : 0L);
    }

    public List<BusOperatorResponse> getAllActiveOperators() {
        List<BusOperator> activeOperators = busOperatorRepository.findByStatus(OperatorStatus.active);
        return activeOperators.stream()
                .map(operator -> new BusOperatorResponse(
                        operator.getId(),
                        operator.getName(),
                        operator.getHotline(),
                        operator.getAddress(),
                        operator.getEmail(), operator.getDescription(),
                        operator.getStatus()))
                .toList();
    }

}
