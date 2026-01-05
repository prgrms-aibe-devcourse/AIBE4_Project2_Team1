package kr.java.pr1mary.entity.lesson;

import jakarta.persistence.*;
import kr.java.pr1mary.entity.BaseEntity;
import kr.java.pr1mary.entity.user.User;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Lesson extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Subjects subjects;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Mode mode;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private double averageRating = 0;

    public enum Mode{
        ONLINE, OFFLINE
    }
}
