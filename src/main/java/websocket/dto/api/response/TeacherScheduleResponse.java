package websocket.dto.api.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class TeacherScheduleResponse {
    private Long scheduleId;
    private Long bookingId;      // 예약이 된 경우만 존재 (없으면 null)
    private String lessonTitle;  // 수업 제목
    private String studentName;  // 예약한 학생 이름 (없으면 "예약 대기")
    private String studentPhone; // 학생 연락처
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;       // AVAILABLE(빈 슬롯), CONFIRMED(예약됨), COMPLETED(완료)
    private String statusKor;    // 화면 표시용 (한글)
}