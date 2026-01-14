package websocket.controller.view;

import websocket.dto.api.response.LessonDetailResponse;
import websocket.entity.lesson.Lesson;
import websocket.entity.lesson.Subjects;
import websocket.service.LessonService;
import websocket.dto.api.response.LessonDetailResponse;
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
        LessonDetailResponse response = lessonService.getLessonDetail(id);

        // 모델에 DTO 담기
        model.addAttribute("lesson", response);
        model.addAttribute("teacherName", "이코딩");

        return "pages/lesson/lesson-detail";
    }
}
