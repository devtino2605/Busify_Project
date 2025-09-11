package com.busify.project.refund.strategy;

import com.busify.project.refund.exception.RefundStrategyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RefundStrategyFactory {

    private final List<RefundStrategy> refundStrategies;

    /**
     * Lấy strategy phù hợp với payment method
     */
    public RefundStrategy getStrategy(String paymentMethod) {
        return refundStrategies.stream()
                .filter(strategy -> strategy.supports(paymentMethod))
                .findFirst()
                .orElseThrow(() -> RefundStrategyNotFoundException.strategyNotFound(paymentMethod));
    }
}
