package websocket.repository;

import websocket.entity.user.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 해당 수업에 작성된 리뷰 불러오기
    @Query("SELECT r FROM Review r WHERE r.lesson.id = :lessonId")
    List<Review> findByLessonId(@Param("lessonId") Long lessonId);

    // 교사에게 작성된 모든 리뷰 불러오기
    @Query("SELECT r FROM Review r WHERE r.lesson.user.id = :teacherId")
    List<Review> findByTeacherId(@Param("teacherId") Long teacherId);

    // 학생이 작성한 모든 리뷰 불러오기
    @Query("SELECT r FROM Review r WHERE r.user.id = :studentId")
    List<Review> findByStudentId(@Param("studentId") Long studentId);

    // 학생이 수업에 작성한 리뷰 불러오기
    Optional<Review> findByUserIdAndLessonId(Long userId, Long lessonId);
}
