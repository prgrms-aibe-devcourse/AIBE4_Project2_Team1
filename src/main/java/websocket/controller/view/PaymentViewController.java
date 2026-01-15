package websocket.controller.view;

import org.springframework.ui.Model;
import websocket.dto.api.request.PaymentConfirmRequest;
import websocket.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentViewController {
    private final PaymentService paymentService;

    // 결제 완료 페이지
    @GetMapping("/success")
    public String paymentSuccess(@RequestParam("bookingId") Long bookingId,
                                 @RequestParam("orderId") String orderId,
                                 @RequestParam("amount") Long amount,
                                 @RequestParam("paymentKey") String paymentKey, Model model) {
        // 결제 승인 요청
        PaymentConfirmRequest paymentConfirmRequest = new PaymentConfirmRequest(bookingId, amount, paymentKey, orderId);
        paymentService.processPayment(paymentConfirmRequest);

        model.addAttribute("orderId", orderId);
        model.addAttribute("amount", amount);

        return "pages/booking/payment-success";
    }

    // 결제 실패
    @GetMapping("/fail")
    public String paymentFail() {
        return "pages/booking/payment-fail";
    }
}
