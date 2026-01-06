package kr.java.pr1mary.dto.api.request;

public record PaymentConfirmRequest(
        Long bookingId,
        Long amount,
        String paymentKey,
        String orderNumber
){
}
