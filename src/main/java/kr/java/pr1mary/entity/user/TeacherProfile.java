package kr.java.pr1mary.entity.user;

import jakarta.persistence.*;
import kr.java.pr1mary.entity.BaseEntity;
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
}
