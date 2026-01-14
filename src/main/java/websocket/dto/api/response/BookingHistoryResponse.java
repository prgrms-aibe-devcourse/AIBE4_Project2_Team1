package websocket.dto.api.response;

import websocket.entity.lesson.Booking;
import websocket.entity.lesson.Lesson;
import websocket.entity.lesson.Subjects;
import websocket.type.BookingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// 👉 예약 현황 조회용
// 메인 페이지 수업 예약 조회 리스트/캘린더 탭
public record BookingHistoryResponse(

        // Booking-History-01 학생 예약 현황 및 TimeTable
        Long bookingId,          // 예약 취소할 때 식별자
        String teacherName,  // 선생님 이름
        String lessonTitle,      // 수업 제목
        Subjects subject,        // 과목 (아이콘 표시용)
        LocalDate startDate, // 수업 시작 일자
        LocalDate endDate,    // 수업 종료 일자
        LocalTime startTime, // 수업 시작 시간
        LocalTime endTime,   // 수업 종료 시간
        String dayOfWeek,      // 수업 요일(영문 ENUM 명칭)
        String teacherEmail,     // 선생님 정보 (추후 이름으로 변경 가능)
        String status,           // 예약 상태 (PENDING, CONFIRMED 등)
        String statusKor,     // 예약 상태 한글 설명
        String location,      // 수업 장소(Zoom, 대면 등)
        String requestMessage    // 학생 요청 메시지
) {
    public static BookingHistoryResponse from(Booking booking){
        // 수업 완료 여부 계산 로직
        LocalDateTime startDateTime = booking.getSchedule().getStartTime();
        LocalDateTime endDateTime = booking.getSchedule().getEndTime();

        BookingStatus currentStatus = booking.getStatus();
        LocalDateTime now = LocalDateTime.now();

        // 기본 한글 설명
        String displayStatus = currentStatus.getDescription();

        // 예약은 확정되었는데 시간이 이미 지난 경우
        if (currentStatus == BookingStatus.CONFIRMED && now.isAfter(endDateTime)) {
            displayStatus = "수업 완료";
        }

        String locationInfo = "장소 미정"; // 기본값

        if(booking.getLesson().getMode() == Lesson.Mode.ONLINE){
            locationInfo = "Zoom (온라인)";
        } else {
            locationInfo = "대면 수업(오프라인)";
        }

        return new BookingHistoryResponse(
                booking.getId(),
                booking.getLesson().getUser().getName(),       // 선생님 성함
                booking.getLesson().getTitle(),               // 수업 제목
                booking.getLesson().getSubjects(),            // 과목
                startDateTime.toLocalDate(),                  // LocalDateTime에서 Date 추출
                endDateTime.toLocalDate(),                    // LocalDateTime에서 Date 추출
                startDateTime.toLocalTime(),                  // LocalDateTime에서 Time 추출
                endDateTime.toLocalTime(),                    // LocalDateTime에서 Time 추출
                startDateTime.getDayOfWeek().name(),          // 요일 추출 (ex: MONDAY)
                booking.getLesson().getUser().getEmail(),
                currentStatus.name(),                         // 상태 코드
                displayStatus,                                // 한글 상태명
                locationInfo,          // Lesson 엔티티에 정의된 장소 정보
                booking.getRequestMessage()                   // 요청 메시지
        );
    }
}
