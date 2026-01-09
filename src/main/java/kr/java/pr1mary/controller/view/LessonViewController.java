package kr.java.pr1mary.controller.view;

import kr.java.pr1mary.entity.lesson.Lesson;
import kr.java.pr1mary.service.LessonService;
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
    public String createForm(){
        return "pages/lesson/create-form";
    }

    // 과외 수정 페이지
    @GetMapping("/update/{lessonId}")
    public String updateLesson(@PathVariable Long lessonId, Model model){
        Lesson lesson = lessonService.getLesson(lessonId);

        model.addAttribute("lesson", lesson);

        return "pages/lesson/update-form";
    }
}
