package kr.java.pr1mary.entity.lesson;

import jakarta.persistence.*;
import kr.java.pr1mary.entity.BaseEntity;
import kr.java.pr1mary.entity.user.User;
import kr.java.pr1mary.type.BookingStatus;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    // @Column(nullable = false)
    // private String status;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(nullable = false)
    private String requestMessage;

    @Column(length = 500)
    private String cancelReason;
}
