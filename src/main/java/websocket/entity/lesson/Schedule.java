package websocket.entity.lesson;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import websocket.dto.api.request.ScheduleRequest;
import websocket.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Boolean isBooked = false;

    public Schedule(ScheduleRequest request, Lesson lesson, User user){
        this.user = user;
        this.lesson = lesson;
        this.startTime = request.startTime();
        this.endTime = request.startTime().plusMinutes(request.duration());
        this.isBooked = false;
    }
}
