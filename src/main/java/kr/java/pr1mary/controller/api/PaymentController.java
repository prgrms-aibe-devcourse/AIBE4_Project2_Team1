package kr.java.pr1mary.controller.api;

import jakarta.validation.Valid;
import kr.java.pr1mary.dto.api.request.PaymentConfirmRequest;
import kr.java.pr1mary.dto.api.response.PaymentResponse;
import kr.java.pr1mary.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // 결제 승인
    @PostMapping("/confirm")
    public ResponseEntity<PaymentResponse> confirmPayment(@RequestBody @Valid PaymentConfirmRequest paymentConfirmRequest) {
        return ResponseEntity.ok(paymentService.processPayment(paymentConfirmRequest));
    }
}
