package websocket.entity.lesson;

import jakarta.persistence.*;
import websocket.dto.api.request.LessonRequest;
import websocket.dto.api.request.LessonUpdateRequest;
import websocket.entity.BaseEntity;
import websocket.entity.user.User;
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
    private double averageRating;

        @Column(nullable = false)
        private Integer timePerSession;

    public enum Mode {
        ONLINE, OFFLINE
    }

    public static Lesson create(LessonRequest lessonRequest, User user) {
        Lesson board = new Lesson();
        board.user = user;
        board.title = lessonRequest.getTitle();
        board.description = lessonRequest.getDescription();
        board.subjects = lessonRequest.getSubjects();
        board.mode = lessonRequest.getMode();
        board.price = lessonRequest.getPrice();
        board.timePerSession = lessonRequest.getTimePerSession();
        board.averageRating = 0.0;

        return board;
    }

    public void update(LessonUpdateRequest lessonUpdateRequest){
        this.title = lessonUpdateRequest.getTitle();
        this.description = lessonUpdateRequest.getDescription();
        this.subjects = lessonUpdateRequest.getSubjects();
        this.mode = lessonUpdateRequest.getMode();
        this.price = lessonUpdateRequest.getPrice();
    }
}
