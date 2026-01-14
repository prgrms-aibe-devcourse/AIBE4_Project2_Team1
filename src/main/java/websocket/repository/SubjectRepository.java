package websocket.repository;

import websocket.entity.lesson.Subject;
import websocket.entity.lesson.Subjects;
import websocket.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
//    @Query("SELECT s FROM Subject s WHERE s.user.id = :teacherId")
//    boolean existsByTeacherIdAndSubject(
//            @Param("teacherId") Long teacherId,
//            @Param("subject") String subject
//    ) {}

    boolean existsByUserAndSubjects(User user, Subjects subjects);
}
