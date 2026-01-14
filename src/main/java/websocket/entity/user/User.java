package websocket.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import websocket.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String email;  // 로그인 ID로 사용

    @Column(nullable = false)
    private String password;  // BCrypt 암호화된 비밀번호

    @Column(nullable = false)
    private String name;

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

    public User(String email, String password, String name, Role role, Auth auth) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.auth = auth;
    }

    // OAuth2 회원가입용 생성자
    public User(String email, String name, Role role, Auth auth) {
        this.email = email;
        // OAuth2 사용자는 비밀번호 없음
        this.password = "";
        this.name = name;
        this.role = role;
        this.auth = auth;
    }
}
