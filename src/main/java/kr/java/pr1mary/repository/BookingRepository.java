package kr.java.pr1mary.repository;

import kr.java.pr1mary.entity.lesson.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Booking-History-01 나의 예약 현황 조회
    List<Booking> findAllByStudentIdOrderByScheduleStartTimeDesc(Long studentId);

    // System-Logic-01 중복 예약 방지 검증용
    boolean existsByScheduleIdAndStatusNot(Long scheduleId, String status);
}
