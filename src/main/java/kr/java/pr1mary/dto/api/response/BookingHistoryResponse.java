package kr.java.pr1mary.dto.api.response;

import kr.java.pr1mary.entity.lesson.Booking;
import kr.java.pr1mary.entity.lesson.Subjects;

import java.time.LocalDateTime;

// 👉 예약 현황 조회용
// 메인 페이지 수업 예약 조회 리스트/캘린더 탭
public record BookingHistoryResponse(

        // Booking-History-01 학생 예약 현황 및 TimeTable
        Long bookingId,          // 예약 취소할 때 식별자
        String lessonTitle,      // 수업 제목
        String teacherEmail,     // 선생님 정보 (추후 이름으로 변경 가능)
        Subjects subject,        // 과목 (아이콘 표시용)
        LocalDateTime startTime, // 수업 시작 시간 (날짜 포함)
        LocalDateTime endTime,   // 수업 종료 시간
        String status,           // 예약 상태 (PENDING, CONFIRMED 등)
        String requestMessage    // 내가 보낸 메시지
) {
    public static BookingHistoryResponse from(Booking booking){
        return new BookingHistoryResponse(
                booking.getId(),
                booking.getLesson().getTitle(), // Lesson 타고 들어가서 제목 가져오기
                booking.getLesson().getUser().getEmail(), // 선생님 이메일
                booking.getLesson().getSubjects(),
                booking.getSchedule().getStartTime(), // Schedule 타고 들어가서 시간 가져오기
                booking.getSchedule().getEndTime(),
                booking.getStatus(),
                booking.getRequestMessage()
        );
    }
}
