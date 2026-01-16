package websocket.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import websocket.entity.user.User;
import websocket.repository.UserRepository;
import websocket.service.LessonService;
import websocket.service.ReviewService;
import websocket.service.TeacherProfileService;

import java.util.NoSuchElementException;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class HomeController {
    private final LessonService lessonService;
    private final TeacherProfileService profileService;
    private final ReviewService reviewService;
    private final UserRepository userRepository;
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("lessons", lessonService.getPopularLessons());
        model.addAttribute("teachers", profileService.getAllTeachers());
        model.addAttribute("reviews", reviewService.getAllReviews());
        return "index";
    }
    @GetMapping("/profile")
    public String profile(HttpSession session) {
        Long id = (Long) session.getAttribute("userId");
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
        return "redirect:/profile/" + (user.getRole() == User.Role.STUDENT ? "student" : "teacher") + "?id=%d".formatted(id);
    }
}
