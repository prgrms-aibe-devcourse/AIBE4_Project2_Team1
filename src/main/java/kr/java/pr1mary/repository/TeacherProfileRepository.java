package kr.java.pr1mary.repository;

import kr.java.pr1mary.entity.user.TeacherProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, Long> {
    boolean existsTeacherProfileByUser_Id(Long userId);
}
