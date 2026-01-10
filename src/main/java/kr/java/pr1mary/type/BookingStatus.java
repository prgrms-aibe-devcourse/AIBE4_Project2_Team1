package kr.java.pr1mary.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BookingStatus {
    PENDING("수락 대기"), // 학생 수강 요청 후 선생님 수락 전
    CONFIRMED("수강 확정"), // 선생님 수락 완료
    REJECTED("수강 신청 거절"), // 선생님 거절
    CANCELLED_BY_STUDENT("학생 취소"), // 학생의 변심 or 사정으로 취소
    CANCELLED_BY_TEACHER("선생님 취소"), // 확정 후 선생님 사정으로 취소
    COMPLETED("수업 완료"); // 시간이 지나 수업 완료

    private final String description;
}
