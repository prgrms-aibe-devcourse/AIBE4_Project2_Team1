package websocket.dto.api.response;

import websocket.entity.lesson.Booking;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookingCreateResponse {

    private Long orderId;         // 예약 번호
    private String orderName;     // 수업 이름
    private String customerName;  // 학생 이름
    private String customerEmail; // 학생 이메일
    private String status;        // 예약 상태 (PENDING)

    // 엔티티 -> DTO 변환 메서드
    public static BookingCreateResponse of(Booking booking) {
        return BookingCreateResponse.builder()
                .orderId(booking.getId())

                // 2. 수업 이름: Booking -> Schedule -> Lesson -> Subject(혹은 Title)
                // (Entity 구조에 따라 getLesson().getTitle() 등 getter 이름 확인 필요)
                .orderName(booking.getLesson().getTitle())

                // 3. 학생 이름: Booking -> Student(User) -> Name
                .customerName(booking.getStudent().getName())

                // 4. 이메일 (결제 알림용)
                .customerEmail(booking.getStudent().getEmail())

                // 5. 상태
                .status(booking.getStatus().name())
                .build();
    }
}