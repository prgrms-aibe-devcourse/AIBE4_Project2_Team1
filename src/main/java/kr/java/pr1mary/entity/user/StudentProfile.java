package kr.java.pr1mary.entity.user;

import jakarta.persistence.*;
import kr.java.pr1mary.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StudentProfile extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String bio;

    @Column(nullable = false)
    private String regionCode;

    @Column(name = "image_url")
    private String imageUrl;
}
