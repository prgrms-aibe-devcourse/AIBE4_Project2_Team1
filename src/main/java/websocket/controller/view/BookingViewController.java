package websocket.controller.view;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import websocket.entity.CustomUserDetails;
import websocket.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingViewController {

    private final ScheduleService scheduleService;

    // 예약 페이지
    @GetMapping("")
    public String bookingPage(
            @RequestParam(required = false) Long scheduleId, // 필수 아님
            @RequestParam(required = false) Long lessonId,   // 필수 아님 (HTML에서 보낸 것)
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        // [수정 포인트 1] DB 연결 잠시 끄기 (테스트용)
        // ScheduleDto schedule = scheduleService.getSchedule(scheduleId);

        // [수정 포인트 2] 화면 확인을 위한 '가짜 데이터' 만들기
        // 어떤 ID로 들어오든 무조건 화면이 뜨게 설정
        model.addAttribute("scheduleId", scheduleId != null ? scheduleId : 1L);
        model.addAttribute("teacherId", 3L);
        model.addAttribute("teacherName", "이코딩 (테스트)");
        model.addAttribute("subject", "고등 수학 완전 정복");
        model.addAttribute("price", 65000L);

        // 날짜도 오늘 날짜로 가짜 생성
        model.addAttribute("dateStr", LocalDateTime.now().toString());

        // 3. 학생 정보 (로그인 기능 연동 전까지 임시 데이터)
        model.addAttribute("studentId", userDetails.getId());
        model.addAttribute("studentName", userDetails.getUsername());
        model.addAttribute("studentPhone", "010-1234-5678");

        return "pages/booking/booking-window";
    }

    // 수강 내역 조회 페이지 - 학생
    @GetMapping("/courseHistory")
    public String bookingHistoryPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails){
        // 1. HTML에서 사용할 studentId 필요
        model.addAttribute("studentId", userDetails.getId());
        // 2. 리턴하는 파일 이름
        return "pages/booking/courseHistory";
    }
}
