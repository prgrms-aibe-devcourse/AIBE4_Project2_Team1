package websocket.controller.view;

import websocket.dto.api.response.LessonDetailResponse;
import websocket.entity.lesson.Lesson;
import websocket.entity.lesson.Subjects;
import websocket.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonViewController {

    private final LessonService lessonService;

    // 과외 생성 페이지
    @GetMapping("/create")
    public String createForm(Model model){
        Long teacherId = 1L;

        model.addAttribute("teacherId", teacherId);

        return "pages/lesson/create-form";
    }

    // 과외 수정 페이지
    @GetMapping("/update/{lessonId}")
    public String updateLesson(@PathVariable Long lessonId, Model model){
        Long teacherId = 1L;

        Lesson lesson = lessonService.getLesson(lessonId);

        model.addAttribute("teacherId", teacherId);
        model.addAttribute("lesson", lesson);

        return "pages/lesson/update-form";
    }

    // 과외 상세 조회 페이지
    @GetMapping("/{id}")
    public String getLessonDetail(@PathVariable Long id, Model model){
        // LessonDetailResponse response = lessonService.getLessonDetail(id);

        // [테스트용 가짜 데이터]
        LessonDetailResponse response = new LessonDetailResponse(
                id,
                "고등 수학 완전 정복 (수1, 수2)",   // title
                "이 수업은 수학 기초가 부족한 학생들을 위해...", // description
                Subjects.MATH,                     // subjects (Enum 가정)
                "ONLINE",                          // mode
                65000L,                            // price (Long 타입)
                4.8,                                // averageRating
                90
        );

        // 모델에 DTO 담기
        model.addAttribute("lesson", response);
        model.addAttribute("teacherName", "이코딩");

        return "pages/lesson/lesson-detail";
    }
}
