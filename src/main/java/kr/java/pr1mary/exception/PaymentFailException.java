package kr.java.pr1mary.exception;

import org.springframework.http.HttpStatus;

public class PaymentFailException extends ApiException {
    private static final String MESSAGE = "Payment failed";

    public PaymentFailException() {
        super(HttpStatus.BAD_REQUEST.value(), MESSAGE);
    }
}
