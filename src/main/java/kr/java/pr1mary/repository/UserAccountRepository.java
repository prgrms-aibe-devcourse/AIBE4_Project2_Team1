package kr.java.pr1mary.repository;

import kr.java.pr1mary.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<User, Long> {

    // 로그인 시 사용자 조회에 사용
    Optional<User> findByEmail(String email);

    // 회원가입 시 중복 체크에 사용
    boolean existsByEmail(String email);

    // OAuth2 사용자 조회용
     Optional<User> findByAuthAndEmail(User.Auth auth, String email);
}
