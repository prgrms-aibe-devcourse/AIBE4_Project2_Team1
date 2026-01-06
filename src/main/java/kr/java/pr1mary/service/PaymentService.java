package kr.java.pr1mary.service;

import jakarta.persistence.EntityNotFoundException;
import kr.java.pr1mary.dto.api.request.PaymentConfirmRequest;
import kr.java.pr1mary.dto.api.response.PaymentResponse;
import kr.java.pr1mary.entity.Payment;
import kr.java.pr1mary.entity.lesson.Booking;
import kr.java.pr1mary.exception.PaymentFailException;
import kr.java.pr1mary.repository.BookingRepository;
import kr.java.pr1mary.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    @Value("${toss.payment.secret-key}")
    private String secretKey;

    public PaymentResponse processPayment(PaymentConfirmRequest paymentConfirmRequest){
        Booking booking = bookingRepository.findById(paymentConfirmRequest.bookingId())
                .orElseThrow(() -> new EntityNotFoundException("예약을 찾을 수 없습니다."));

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
                    .header("Authorization", getAuthorizations())
                    .header("Content-Type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString("{\"paymentKey\":\"" + paymentConfirmRequest.paymentKey()
                            + "\",\"orderId\":\"" + paymentConfirmRequest.orderNumber()
                            + "\",\"amount\":" + paymentConfirmRequest.amount() + "}"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200){
                Payment payment = new Payment(paymentConfirmRequest, booking);
                paymentRepository.save(payment);

                return PaymentResponse.from(payment);
            } else {
                throw new PaymentFailException();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String getAuthorizations() {
        final byte[] encodedBytes = Base64.getEncoder().encode((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedBytes);
    }
}
