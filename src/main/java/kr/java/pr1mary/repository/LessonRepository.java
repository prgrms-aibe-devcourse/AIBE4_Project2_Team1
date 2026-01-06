package kr.java.pr1mary.repository;

import kr.java.pr1mary.entity.lesson.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByUser_Id(Long userId);

    @Query("select l from Lesson l order by l.averageRating desc limit 10")
    List<Lesson> findPopularLessons();
}
