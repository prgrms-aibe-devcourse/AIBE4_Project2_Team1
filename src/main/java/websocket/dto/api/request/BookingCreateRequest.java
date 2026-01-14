package websocket.dto.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookingCreateRequest(

        // Class-Booking-02 수업 예약 요청
        // @NotNull(message = "학생 ID는 필수입니다.")
        // Long studentId,

        @NotNull(message="스케줄 ID는 필수입니다")
        Long scheduleId,

        @Size(max = 500, message = "요청 메시지는 500자를 초과할 수 없습니다")
        String requestMessage
) {
}
