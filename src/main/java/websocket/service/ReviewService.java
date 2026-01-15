package websocket.service;

import org.springframework.transaction.annotation.Transactional;
import websocket.dto.view.ReviewDTO;
import websocket.entity.user.Review;
import websocket.repository.BookingRepository;
import websocket.repository.LessonRepository;
import websocket.repository.ReviewRepository;
import websocket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final BookingRepository bookingRepository;

    // 리뷰 저장
    @Transactional
    public void saveReview(ReviewDTO dto) {
        Review review = new Review();
        review.setUser(userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없음")));
        review.setLesson(lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new NoSuchElementException("해당 수업을 찾을 수 없음")));
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        reviewRepository.save(review);
    }

    // 해당 수업에 작성된 리뷰 불러오기
    public List<ReviewDTO> loadReviewAboutLesson(Long lessonId) {
        return reviewRepository.findByLessonId(lessonId).stream().map(this::toDTO).toList();
    }

    // 교사에게 작성된 리뷰 불러오기
    public List<ReviewDTO> loadReviewAboutTeacher(Long teacherId) {
        return reviewRepository.findByTeacherId(teacherId).stream().map(this::toDTO).toList();
    }

    // 학생이 작성한 리뷰 불러오기
    public List<ReviewDTO> loadReviewByStudent(Long studentId) {
        return reviewRepository.findByStudentId(studentId).stream().map(this::toDTO).toList();
    }

    // 학생이 수업에 작성한 리뷰 불러오기
    public ReviewDTO loadReviewByStudentAndLesson(Long studentId, Long lessonId) {
        return toDTO(reviewRepository.findByUserIdAndLessonId(studentId, lessonId)
                .orElseGet(() -> new Review(
                        userRepository.findById(studentId).orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없음")),
                        lessonRepository.findById(lessonId).orElseThrow(() -> new NoSuchElementException("해당 수업을 찾을 수 없음"))
                )));
    }

    private ReviewDTO toDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setUserId(review.getUser().getId());
        dto.setLessonId(review.getLesson().getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        return dto;
    }
}
