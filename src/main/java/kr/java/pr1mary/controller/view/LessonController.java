package kr.java.pr1mary.controller.view;

import jakarta.validation.Valid;
import kr.java.pr1mary.dto.view.LessonForm;
import kr.java.pr1mary.dto.view.LessonUpdateForm;
import kr.java.pr1mary.entity.lesson.Lesson;
import kr.java.pr1mary.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    // 과외 생성 페이지
    @GetMapping("/create")
    public String createForm(Model model){
        model.addAttribute("lessonForm", new LessonForm());

        return "pages/lesson/create-form";
    }

    // 과외 수정 페이지
    @GetMapping("/update/{lessonId}")
    public String updateLesson(@PathVariable Long lessonId, Model model){
        Lesson lesson = lessonService.getLesson(lessonId);

        model.addAttribute("lessonId", lessonId);
        model.addAttribute("lessonUpdateForm", new LessonUpdateForm(lesson));

        return "pages/lesson/update-form";
    }

    // 과외 생성
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute LessonForm lessonForm) {
        // TODO: 저장 로직 구현하기
        // lessonService.saveLesson(lessonForm, 1L);

        return "redirect:/lessons/create";
    }

    // 과외 삭제
    @DeleteMapping("/{lessonId}")
    public String deleteLesson(@PathVariable Long lessonId){
        //lessonService.deleteLesson(lessonId, 1L);

        return "redirect:/lessons";
    }
}
