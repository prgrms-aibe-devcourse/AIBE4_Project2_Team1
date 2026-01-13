package kr.java.pr1mary.controller.view;

import kr.java.pr1mary.dto.view.ScheduleDto;
import kr.java.pr1mary.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingViewController {

    private final ScheduleService scheduleService;

    // 예약 페이지
    @GetMapping("")
    public String bookingPage(@RequestParam(defaultValue = "501") Long scheduleId, Model model) {

        // 1. DB에서 실제 수업 정보 가져오기
        ScheduleDto schedule = scheduleService.getSchedule(scheduleId);

        // 2. 모델에 데이터 담기
        model.addAttribute("scheduleId", schedule.scheduleId());
        model.addAttribute("teacherId", schedule.teacherId());
        model.addAttribute("teacherName", schedule.teacherName());
        model.addAttribute("subject", schedule.subject());
        model.addAttribute("price", schedule.price());
        model.addAttribute("dateStr", schedule.startDateTime().toString());

        // 3. 학생 정보( 로그인 기능 연동 전까지 임시 데이터 )
        model.addAttribute("studentId", 2001L);
        model.addAttribute("studentName", "김학생");
        model.addAttribute("studentPhone", "010-1234-5678");

        return "pages/booking/booking-window";
    }

    // 수강 내역 조회 페이지 - 학생
    @GetMapping("/courseHistory")
    public String bookingHistoryPage(Model model){
        // 1. HTML에서 사용할 studentId 필요
        model.addAttribute("studentId", 2001L);
        // 2. 리턴하는 파일 이름
        return "pages/booking/courseHistory";
    }
}
