package websocket.repository;

import websocket.entity.user.TeacherProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, Long> {
    boolean existsTeacherProfileByUser_Id(Long userId);
}
