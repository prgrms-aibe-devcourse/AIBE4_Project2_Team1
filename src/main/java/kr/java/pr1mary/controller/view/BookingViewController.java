package kr.java.pr1mary.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingViewController {

    // 예약 페이지
    @GetMapping
    public String bookingPage(){
        return "pages/booking/booking-window";
    }
}
