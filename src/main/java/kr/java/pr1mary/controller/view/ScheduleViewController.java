package kr.java.pr1mary.controller.view;

import kr.java.pr1mary.dto.api.response.TeacherScheduleResponse;
import kr.java.pr1mary.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

// 선생님 시간표 조회
@Controller
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class ScheduleViewController {
    private final ScheduleService scheduleService;

    // 선생님 페이지 - 시간표 관리 화면
    @GetMapping("/timetable")
    public String teacherTimetable(Model model) {
        // TODO : 로그인 기능 구현 후 @AuthenticationPrincipal 로 ID를 받아와야 함
        // 테스트 용으로 101L 사용
        Long currentTeacherId = 101L;

        List<TeacherScheduleResponse> schedules = scheduleService.getTeacherSchedules(currentTeacherId);

        model.addAttribute("schedules", schedules);

        return "pages/schedule/teacher-timetable";
    }
}
