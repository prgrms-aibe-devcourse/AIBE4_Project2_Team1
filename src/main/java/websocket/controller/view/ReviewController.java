package websocket.controller.view;

import websocket.dto.view.ReviewDTO;
import websocket.service.ReviewService;
import websocket.service.StudentProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final StudentProfileService studentProfileService;
    private final ReviewService reviewService;

    // 리뷰 작성 및 수정 페이지 => 학생이 수업을 대상으로
    @GetMapping
    public String reviewPage(
            @RequestParam("userId") Long userId,
            @RequestParam("lessonId") Long lessonId,
            Model model
    ) {
        model.addAttribute("userId", userId);
        model.addAttribute("lessonId", lessonId);
        model.addAttribute("student", studentProfileService.getProfileByStudentId(userId));
        model.addAttribute("review", reviewService.loadReviewByStudentAndLesson(userId, lessonId));
        return "pages/lesson/review";
    }

    @PostMapping
    public String insertReview(@RequestBody ReviewDTO dto) {
        reviewService.saveReview(dto);
        return "redirect:/profile/student?id=%d".formatted(dto.getUserId());
    }

//    // 학생이 작성한 리뷰 불러오기
//    @GetMapping("/student")
//    public ResponseEntity<List<ReviewDTO>> getStudentReview(@RequestParam("id") Long studentId) {
//        List<ReviewDTO> reviews = reviewService.loadReviewByStudent(studentId);
//        return ResponseEntity.ok(reviews);
//    }
//
//    // 교사에게 작성된 리뷰 불러오기
//    @GetMapping("/teacher")
//    public ResponseEntity<List<ReviewDTO>> getteacherReview(@RequestParam("id") Long teacherId) {
//        List<ReviewDTO> reviews = reviewService.loadReviewAboutTeacher(teacherId);
//        return ResponseEntity.ok(reviews);
//    }
//
//    // 수업에 작성된 리뷰 불러오기
//    @GetMapping("/lesson")
//    public ResponseEntity<List<ReviewDTO>> getLessonReview(@RequestParam("id") Long lessonId) {
//        List<ReviewDTO> reviews = reviewService.loadReviewAboutLesson(lessonId);
//        return ResponseEntity.ok(reviews);
//    }
}
