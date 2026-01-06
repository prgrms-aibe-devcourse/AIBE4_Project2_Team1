package kr.java.pr1mary.controller.view;

import jakarta.validation.Valid;
import kr.java.pr1mary.dto.view.LessonForm;
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

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute LessonForm lessonForm) {
        // TODO: 저장 로직 구현하기
        //lessonService.save(lessonForm, 1L);

        return "redirect:/lessons/create";
    }

}
