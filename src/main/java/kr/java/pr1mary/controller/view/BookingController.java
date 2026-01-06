package kr.java.pr1mary.controller.view;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {
    @GetMapping
    public String bookingPage(){
        return "pages/booking/booking-window";
    }

    // 결제 완료 페이지
    @GetMapping("/success")
    public String paymentSuccess(@RequestParam("orderId") String orderNumber,
                                 @RequestParam("amount") int amount,
                                 @RequestParam("paymentKey") String paymentKey,
                                 Model model) {
        model.addAttribute("paymentKey", paymentKey);
        model.addAttribute("orderNumber", orderNumber);
        model.addAttribute("amount", amount);

        return "pages/booking/payment-success";
    }

    // 결제 실패
    @GetMapping("/fail")
    public String paymentFail() {
        return "pages/booking/payment-fail";
    }
}
