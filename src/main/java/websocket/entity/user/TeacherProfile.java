package websocket.entity.user;

import jakarta.persistence.*;
import websocket.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TeacherProfile extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String bio;

    @Column(nullable = false)
    private String regionCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Level schoolLevel;

    private String imageUrl;

    public enum Level {
        ELEMENTARY, MIDDLE, HIGH, UNIVERSITY
    }

    public TeacherProfile(User user) {
        this.user = user;
        this.bio = "안녕하세요, 새로운 교사입니다.";
        this.regionCode = "대한민국 어딘가";
        this.schoolLevel = Level.UNIVERSITY;
    }

    public TeacherProfile() {

    }
}
