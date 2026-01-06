package kr.java.pr1mary.repository;

import kr.java.pr1mary.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
