package com.busify.project.payment.strategy;

import com.busify.project.payment.enums.PaymentMethod;
import com.busify.project.payment.exception.PaymentMethodException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentStrategyFactory {

    private final List<PaymentStrategy> paymentStrategies;

    /**
     * Lấy strategy phù hợp với payment method
     * 
     * @param paymentMethod Payment method cần xử lý
     * @return PaymentStrategy tương ứng
     * @throws RuntimeException nếu không tìm thấy strategy phù hợp
     */
    public PaymentStrategy getStrategy(PaymentMethod paymentMethod) {
        return paymentStrategies.stream()
                .filter(strategy -> strategy.supports(paymentMethod.name()))
                .findFirst()
                .orElseThrow(() -> PaymentMethodException.methodNotSupported());
    }

    /**
     * Kiểm tra payment method có được hỗ trợ không
     * 
     * @param paymentMethod Payment method cần kiểm tra
     * @return true nếu có strategy hỗ trợ
     */
    public boolean isSupported(PaymentMethod paymentMethod) {
        return paymentStrategies.stream()
                .anyMatch(strategy -> strategy.supports(paymentMethod.name()));
    }
}
