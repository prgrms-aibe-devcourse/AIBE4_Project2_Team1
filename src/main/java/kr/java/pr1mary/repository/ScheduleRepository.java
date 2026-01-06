package kr.java.pr1mary.repository;

import kr.java.pr1mary.entity.lesson.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    // 특정 선생님의 예약 가능한 스케줄 조회
    List<Schedule> findAllByUserIdAndIsBookedFalseOrderByStartTimeAsc(Long teacherId);
    // 특정 날짜 범위의 스케줄 조회(캘린더 뷰)
    List<Schedule> findAllByUserIdAndStartTimeBetween(Long teacherId, LocalDateTime start, LocalDateTime end);

    List<Schedule> findAllByUserIdAndStartTimeBetweenAndIsBookedFalseOrderByStartTimeAsc(Long teacherId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}