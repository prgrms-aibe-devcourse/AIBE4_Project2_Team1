package kr.java.pr1mary.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kr.java.pr1mary.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String email;  // 로그인 ID로 사용

    @Column(nullable = false)
    private String password;  // BCrypt 암호화된 비밀번호

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Auth auth;

    // 권한을 나타내는 열거형
    public enum Role {
        STUDENT, TEACHER, ADMIN
    }

    // 로그인 형태
    public enum Auth {
        LOCAL, GOOGLE, KAKAO
    }
}
