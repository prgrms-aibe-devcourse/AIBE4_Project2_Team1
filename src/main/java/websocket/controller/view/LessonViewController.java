package websocket.controller.view;

import websocket.entity.lesson.Lesson;
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
        // TODO : 로그인 기능 구현 후 @AuthenticationPrincipal 로 ID를 받아와야 함
        Long teacherId = 1L;

        model.addAttribute("teacherId", teacherId);

        return "pages/lesson/create-form";
    }

    // 과외 수정 페이지
    @GetMapping("/update/{lessonId}")
    public String updateLesson(@PathVariable Long lessonId, Model model){
        // TODO : 로그인 기능 구현 후 @AuthenticationPrincipal 로 ID를 받아와야 함
        Long teacherId = 1L;

        Lesson lesson = lessonService.getLesson(lessonId);

        model.addAttribute("teacherId", teacherId);
        model.addAttribute("lesson", lesson);

        return "pages/lesson/update-form";
    }
}
