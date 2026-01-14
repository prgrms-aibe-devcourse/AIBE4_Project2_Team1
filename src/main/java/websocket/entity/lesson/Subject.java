package websocket.entity.lesson;

import jakarta.persistence.*;
import websocket.entity.user.User;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Subjects subjects;
}
