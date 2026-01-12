package kr.java.pr1mary.repository;

import kr.java.pr1mary.entity.lesson.Booking;
import kr.java.pr1mary.type.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // 1. 나의 예약 현황 조회
    List<Booking> findAllByStudentIdOrderByScheduleStartTimeDesc(Long studentId);

    // 2. 중복 예약 방지
    boolean existsByScheduleIdAndStatusNot(Long scheduleId, BookingStatus status);
}