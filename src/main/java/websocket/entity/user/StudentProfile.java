package websocket.entity.user;

import jakarta.persistence.*;
import websocket.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StudentProfile extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String bio;

    @Column(nullable = false)
    private String regionCode;

    @Column(name = "image_url")
    private String imageUrl;

    public StudentProfile(User user) {
        this.user = user;
        this.bio = "안녕하세요, 새로운 학생입니다.";
        this.regionCode = "대한민국 어딘가";
    }

    public StudentProfile() {

    }
}
