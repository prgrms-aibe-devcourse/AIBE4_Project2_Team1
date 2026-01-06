package kr.java.pr1mary.dto.api.response;

import kr.java.pr1mary.entity.Payment;
import lombok.Builder;

@Builder
public record PaymentResponse(
        Long id,
        Long amount,
        String orderNumber
) {
        public static PaymentResponse from(Payment payment) {
            return PaymentResponse.builder()
                    .id(payment.getId())
                    .amount(payment.getAmount())
                    .orderNumber(payment.getOrderNumber())
                    .build();
        }
}
