package kr.java.pr1mary.controller.view;

import kr.java.pr1mary.dto.view.ScheduleDto;
import kr.java.pr1mary.entity.lesson.Lesson;
import kr.java.pr1mary.entity.user.User;
import kr.java.pr1mary.repository.LessonRepository;
import kr.java.pr1mary.repository.UserRepository;
import kr.java.pr1mary.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingViewController {

    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    // 예약 페이지
    @GetMapping("")
    public String bookingPage(
            @RequestParam Long lessonId,
            @RequestParam Long studentId,
            Model model
    ){
        // 수업 정보 조회
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 수업입니다."));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생입니다."));

        // 모델에 실제 데이터 담기
        model.addAttribute("lessonId", lesson.getId());
        model.addAttribute("subject", lesson.getTitle());
        model.addAttribute("price", lesson.getPrice());

        model.addAttribute("teacherId", lesson.getUser().getId());
        model.addAttribute("teacherName", lesson.getUser().getName());

        model.addAttribute("dateStr", LocalDate.now().toString());

        // 3. 학생 정보 (로그인 기능 연동 전까지 임시 데이터)
        model.addAttribute("studentId", student.getId());
        model.addAttribute("studentName", student.getName());

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

    // 결제 성공 페이지
    @GetMapping("/success")
    public String successPage() {
        return "booking-success";
    }

    // 결제 실패 페이지
    @GetMapping("/fail")
    public String failPage() {
        return "booking-fail";
    }
}
