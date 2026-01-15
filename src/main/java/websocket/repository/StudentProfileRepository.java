package websocket.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import websocket.entity.user.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    @Query("select s from StudentProfile s join fetch s.user where s.user.id = :id")
    Optional<StudentProfile> findByUser_Id(@Param("id") Long id);
}
