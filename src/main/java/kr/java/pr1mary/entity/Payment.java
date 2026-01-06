package kr.java.pr1mary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.java.pr1mary.dto.api.request.PaymentConfirmRequest;
import kr.java.pr1mary.entity.lesson.Booking;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
public class Payment extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String paymentKey;

    @Column(nullable = false)
    private String orderNumber;

    public Payment(PaymentConfirmRequest paymentConfirmRequest, Booking booking){
        this.booking = booking;
        this.amount = paymentConfirmRequest.amount();
        this.paymentKey = paymentConfirmRequest.paymentKey();
        this.orderNumber = paymentConfirmRequest.orderNumber();
    }
}
