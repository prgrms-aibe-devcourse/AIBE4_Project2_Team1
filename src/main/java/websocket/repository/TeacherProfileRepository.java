package websocket.repository;

import org.springframework.data.jpa.repository.Query;
import websocket.entity.user.TeacherProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, Long> {
    @Query("select t from TeacherProfile t join fetch t.user where t.user.id = :id")
    Optional<TeacherProfile> findByUser_Id(Long id);

    boolean existsTeacherProfileByUser_Id(Long userId);
}
