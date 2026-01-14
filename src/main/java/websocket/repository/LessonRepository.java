package websocket.repository;

import websocket.entity.lesson.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByUser_Id(Long userId);

    @Query("select l from Lesson l order by l.averageRating desc limit 10")
    List<Lesson> findPopularLessons();

    @Query("select l from Lesson l where l.title like %:content% or l.user.name like %:content%")
    Page<Lesson> findLessonsByContaining(String content, Pageable pageable);
}
