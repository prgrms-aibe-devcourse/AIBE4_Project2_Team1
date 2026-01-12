package kr.java.pr1mary.repository;

import jakarta.persistence.LockModeType;
import kr.java.pr1mary.entity.lesson.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // 특정 선생님의 예약 가능한 스케줄 조회
    List<Schedule> findAllByUserIdAndIsBookedFalseOrderByStartTimeAsc(Long teacherId);

// 특정 날짜 범위의 스케줄 조회(캘린더 뷰)
    List<Schedule> findAllByUserIdOrderByStartTimeDesc(Long teacherId);

    List<Schedule>findAllByUserIdAndStartTimeBetweenOrderByStartTimeAsc(
            Long teacherId,
            LocalDateTime start,
            LocalDateTime end
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Schedule s where s.id = :id")
    Optional<Schedule> findByIdWithLock(@Param("id") Long id);
}