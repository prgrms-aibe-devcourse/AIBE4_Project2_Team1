package websocket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import websocket.service.LessonService;
import websocket.service.ReviewService;
import websocket.service.TeacherProfileService;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class HomeController {
    private final LessonService lessonService;
    private final TeacherProfileService profileService;
    private final ReviewService reviewService;
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("lessons", lessonService.getPopularLessons());
        model.addAttribute("teachers", profileService.getAllTeachers());
        model.addAttribute("reviews", reviewService.getAllReviews());
        return "index";
    }
}
